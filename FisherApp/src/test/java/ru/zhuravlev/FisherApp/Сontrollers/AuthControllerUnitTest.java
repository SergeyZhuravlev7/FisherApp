package ru.zhuravlev.FisherApp.Сontrollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthManager;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthProviderImpl;
import ru.zhuravlev.FisherApp.DTOs.LoginDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOIn;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @InjectMocks
    private AuthController authController;

    private UserDTOIn userDTOIn;
    private User user;

    @BeforeEach
    void setUp() {
        userDTOIn = new UserDTOIn("TestLogin", "Иван", "password", 50, Gender.MALE);
        user = new User();
    }

    @Test
    void registrationWithValidUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(userDTOIn, User.class))
                .thenReturn(user);

        ResponseEntity<HttpStatus> response = authController.registration(userDTOIn, bindingResult);
        assertEquals(200, response.getStatusCode().value());

        verify(userService).save(user);
    }

    @Test
    void registrationWithInvalidUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(UserFieldsException.class, () -> authController.registration(userDTOIn, bindingResult));
    }

    @Test
    void registrationWithExistingUser() {
        when(userService.findByLogin(userDTOIn.getLogin())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> authController.registration(userDTOIn, bindingResult));
    }



    @Test
    void loginWithInvalidUser() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(UserFieldsException.class, () -> authController.login(new LoginDTO(), bindingResult));
    }

    @Test
    void loginWithValidUser() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));

        ResponseEntity<HttpStatus> response = authController.login(new LoginDTO(), bindingResult);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void  UserFieldsExceptionHandler() {
        HashMap<String,String> map = new HashMap<>();
        map.put("login", "loginMessage");
        UserFieldsException exception = new UserFieldsException(map);
        ResponseEntity<HashMap<String,String>> response = authController.exceptionHandler(exception);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("loginMessage", response.getBody().get("login"));
    }

    @Test
    void userAlreadyExistExceptionHandler() {
        UserAlreadyExistException exception = new UserAlreadyExistException();
        ResponseEntity<UserErrorResponse> response = authController.exceptionHandler(exception);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Пользователь с таким логином уже зарегистрирован.", response.getBody().getMessage());
        assertTrue(response.getBody().getTimestamp() > 0);
    }
}