package ru.zhuravlev.FisherApp.Сontrollers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthManager;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthProviderImpl;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetails;
import ru.zhuravlev.FisherApp.DTOs.LoginDTO;
import ru.zhuravlev.FisherApp.DTOs.TokenDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.JWTService;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.BindingResultConverter;
import ru.zhuravlev.FisherApp.Util.UserAlreadyExistException;
import ru.zhuravlev.FisherApp.Util.UserErrorResponse;
import ru.zhuravlev.FisherApp.Util.UserFieldsException;
import ru.zhuravlev.FisherApp.Validators.UserDTOInValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith (MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private BindingResultConverter converter;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthManager authenticationManager;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private AuthProviderImpl authProvider;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserDTOInValidator userDTOInValidator;

    @InjectMocks
    private AuthController authController;

    private UserDTOIn userDTOIn;
    private User user;

    @BeforeEach
    void setUp() {
        userDTOIn = new UserDTOIn("TestLogin","password","Иван","08.02.1995",Gender.MALE,"testmail@gmail.com");
        user = new User();
    }

    @Test
    void registrationWithValidUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(userDTOIn,User.class))
                .thenReturn(user);

        ResponseEntity<HttpStatus> response = authController.registration(userDTOIn,bindingResult);
        assertEquals(200,response.getStatusCode().value());

        verify(userService).save(user);
    }

    @Test
    void registrationWithInvalidUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(UserFieldsException.class,() -> authController.registration(userDTOIn,bindingResult));
    }

    @Test
    void registrationWithExistingUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class,() -> authController.registration(userDTOIn,bindingResult));
    }


    @Test
    void loginWithInvalidUser() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(UserFieldsException.class,() -> authController.login(new LoginDTO(),bindingResult));
    }

    @Test
    void loginWithValidUser() {
        Authentication authentication = mock(Authentication.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mock(CustomUserDetails.class));
        when(jwtService.createAccessToken(any(CustomUserDetails.class))).thenReturn("TestAccessToken");
        when(jwtService.createRefreshToken(any(CustomUserDetails.class))).thenReturn("TestRefreshToken");

        ResponseEntity<Map<String, String>> response = authController.login(new LoginDTO(),bindingResult);

        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("accessToken"));
        assertTrue(response.getBody().containsKey("refreshToken"));
    }

    @Test
    void refreshTokenWithValidToken() {
        TokenDTO token = new TokenDTO();
        token.setRefreshToken("TestRefreshToken");
        when(jwtService.isValidRefreshToken(token.getRefreshToken())).thenReturn(true);
        when(jwtService.createAccessToken(token.getRefreshToken())).thenReturn("NewTestAccessToken");

        ResponseEntity<Map<String, String>> result = authController.refreshAccessToken(token);

        assertEquals(200,result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("NewTestAccessToken",result.getBody().get("accessToken"));
    }

    @Test
    void refreshTokenWithInvalidToken() {
        TokenDTO token = new TokenDTO();
        token.setRefreshToken("TestRefreshToken");
        when(jwtService.isValidRefreshToken(token.getRefreshToken())).thenReturn(false);

        ResponseEntity<Map<String, String>> result = authController.refreshAccessToken(token);

        assertEquals(401,result.getStatusCode().value());
        assertNull(result.getBody());
    }

    @Test
    void refreshTokenWithThrowException() {
        TokenDTO token = new TokenDTO();
        token.setRefreshToken("TestRefreshToken");
        when(jwtService.isValidRefreshToken(token.getRefreshToken())).thenThrow(JWTDecodeException.class);
        assertThrows(JWTDecodeException.class,() -> authController.refreshAccessToken(token));
    }

    @Test
    void UserFieldsExceptionHandler() {
        HashMap<String, String> map = new HashMap<>();
        map.put("login","loginMessage");
        UserFieldsException exception = new UserFieldsException(map);
        ResponseEntity<Map<String, String>> response = authController.exceptionHandler(exception);
        assertEquals(400,response.getStatusCode().value());
        assertEquals("loginMessage",response.getBody().get("login"));
    }

    @Test
    void userAlreadyExistExceptionHandler() {
        UserAlreadyExistException exception = new UserAlreadyExistException();
        ResponseEntity<UserErrorResponse> response = authController.exceptionHandler(exception);
        assertEquals(400,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Пользователь с таким логином уже зарегистрирован.",response.getBody().getMessage());
        assertTrue(response.getBody().getTimestamp() > 0);
    }
}