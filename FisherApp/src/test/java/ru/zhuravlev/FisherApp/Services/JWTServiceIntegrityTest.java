package ru.zhuravlev.FisherApp.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetails;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.User;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTServiceIntegrityTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    User testUser;
    CustomUserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        testUser = new User("TestLogin", passwordEncoder.encode("password"), "Иван", 50, Gender.MALE);
        testUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    void createAccessTokenFromUserDetails() {
        String accessToken = jwtService.createAccessToken(testUserDetails);

        assertNotNull(accessToken);
        assertEquals(3,accessToken.split("[.]").length);
    }

    @Test
    void testCreateAccessTokenFromString() {
        String refreshToken = jwtService.createRefreshToken(testUserDetails);

        String accessToken = jwtService.createAccessToken(refreshToken);

        assertNotNull(accessToken);
        assertEquals(3,accessToken.split("[.]").length);
    }

    @Test
    void createRefreshToken() {
        String refreshToken = jwtService.createRefreshToken(testUserDetails);

        assertNotNull(refreshToken);
        assertEquals(3,refreshToken.split("[.]").length);
    }

    @Test
    void getUsername() {
        String accessToken = jwtService.createAccessToken(testUserDetails);

        String subject = jwtService.getUsername(accessToken);

        assertEquals("TestLogin", subject);
    }

    @Test
    void isValidAccessToken() {
        String accessToken = jwtService.createAccessToken(testUserDetails);
        assertTrue(jwtService.isValidAccessToken(accessToken, testUserDetails));
    }

    @Test
    void isInvalidAccessToken() {
        String accessToken = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("alg","HMAC256")
                .withClaim("typ","access")
                .withIssuer("FisherApp")
                .withIssuedAt(new Date())
                .withSubject("InvalidUserName")
                .withClaim("ROLES", "ROLE_USER")
                .withExpiresAt(new Date())
                .sign(Algorithm.HMAC256("testkey"));
        assertFalse(jwtService.isValidAccessToken(accessToken, testUserDetails));
        assertTrue(JWT.decode(accessToken).getExpiresAt().before(new Date()));
    }

    @Test
    void isValidRefreshToken() {
        String refreshToken = jwtService.createRefreshToken(testUserDetails);
        assertTrue(jwtService.isValidAccessToken(refreshToken, testUserDetails));
    }

    @Test
    void isInvalidRefreshToken() {
        String refreshToken = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("alg","HMAC256")
                .withClaim("typ","access")
                .withIssuer("FisherApp")
                .withIssuedAt(new Date())
                .withSubject("InvalidUserName")
                .withClaim("ROLES", "ROLE_USER")
                .withExpiresAt(new Date())
                .sign(Algorithm.HMAC256("testkey"));
        assertFalse(jwtService.isValidAccessToken(refreshToken, testUserDetails));
        assertTrue(JWT.decode(refreshToken).getExpiresAt().before(new Date()));
    }
}