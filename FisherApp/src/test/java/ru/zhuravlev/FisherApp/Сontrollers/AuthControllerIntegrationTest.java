package ru.zhuravlev.FisherApp.Сontrollers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetails;
import ru.zhuravlev.FisherApp.DTOs.LoginDTO;
import ru.zhuravlev.FisherApp.DTOs.TokenDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.JWTService;
import ru.zhuravlev.FisherApp.Services.UserService;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private JWTService jwtService;

    private UserDTOIn validUserDTO;
    private UserDTOIn invalidUserDTO;
    private User testUser;
    private LoginDTO testLoginDTO;

    @BeforeEach
    void setUp() {
        validUserDTO = new UserDTOIn("TestLogin", "Иван", "password", 50, Gender.MALE);
        invalidUserDTO = new UserDTOIn();
        testUser = new User("TestLogin", passwordEncoder.encode("password"), "Иван", 50, Gender.MALE);
        testUser.setRole("USER");
        testLoginDTO = new LoginDTO();
        testLoginDTO.setLogin("TestLogin");
        testLoginDTO.setPassword("password");
    }

    @Test
    @WithAnonymousUser
    void registrationWithExistingUsername() throws Exception {
        when(userService.findByLogin("TestLogin")).thenReturn(Optional.of(testUser));
        mockMvc.perform(post("/api/auth/registration")
                .content(objectMapper.writeValueAsString(validUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").hasJsonPath())
                .andExpect(jsonPath("timestamp").hasJsonPath())
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithAnonymousUser
    void registrationWithValidUser() throws Exception {
        when(userService.findByLogin("TestLogin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/auth/registration")
                        .content(objectMapper.writeValueAsString(validUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @WithAnonymousUser
    void registrationWithInvalidUser() throws Exception {
        when(userService.findByLogin("TestLogin")).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/auth/registration")
                        .content(objectMapper.writeValueAsString(invalidUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("login").hasJsonPath())
                .andExpect(jsonPath("name").hasJsonPath())
                .andExpect(jsonPath("age").hasJsonPath())
                .andExpect(jsonPath("gender").hasJsonPath())
                .andExpect(jsonPath("password").hasJsonPath());
    }

    @Test
    @WithMockUser(username = "someName123")
    void registrationWithAuthUser() throws Exception {
        mockMvc.perform(post("/api/auth/registration")
                    .content(objectMapper.writeValueAsString(validUserDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "someName123")
    void loginWithAuthUser() throws Exception {
        mockMvc.perform(post("/api/auth/registration")
                        .content(objectMapper.writeValueAsString(testLoginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithAnonymousUser
    void loginWithExistingUser () throws Exception {
        when(userService.findByLogin(testLoginDTO.getLogin())).thenReturn(Optional.of(testUser));
        mockMvc.perform(post("/api/auth/login")
                    .content(objectMapper.writeValueAsString(testLoginDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").hasJsonPath())
                .andExpect(jsonPath("refreshToken").hasJsonPath());;
    }

    @Test
    @WithAnonymousUser
    void loginWithoutExistingUser () throws Exception {
        when(userService.findByLogin(testLoginDTO.getLogin())).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(testLoginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    @WithAnonymousUser
    void refreshTokenWithValidToken() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String stringToken = jwtService.createRefreshToken(userDetails);
        TokenDTO validToken = new TokenDTO();
        validToken.setRefreshToken(stringToken);

        mockMvc.perform(post("/api/auth/token")
                    .content(objectMapper.writeValueAsString(validToken))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("accessToken").hasJsonPath());
    }

    @Test
    @WithAnonymousUser
    void refreshTokenWithInvalidToken() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String stringToken = jwtService.createRefreshToken(userDetails);
        TokenDTO invalidToken = new TokenDTO();
        invalidToken.setRefreshToken(stringToken);
        when(jwtService.isValidRefreshToken(invalidToken.getRefreshToken())).thenReturn(false);

        mockMvc.perform(post("/api/auth/token")
                        .content(objectMapper.writeValueAsString(invalidToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andExpect(jsonPath("accessToken").doesNotHaveJsonPath());
    }

    @Test
    @WithAnonymousUser
    void refreshTokenWithException() throws Exception {
        String stringToken = "ActuallyInvalidToken";
        TokenDTO invalidToken = new TokenDTO();
        invalidToken.setRefreshToken(stringToken);

        mockMvc.perform(post("/api/auth/token")
                        .content(objectMapper.writeValueAsString(invalidToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("accessToken").doesNotHaveJsonPath())
                .andExpect(jsonPath("refreshToken").hasJsonPath());
    }
}