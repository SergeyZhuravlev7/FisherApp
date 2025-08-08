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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import ru.zhuravlev.FisherApp.DTOs.PostDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOOut;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.*;
import ru.zhuravlev.FisherApp.Validators.PostDTOValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith (MockitoExtension.class)
class MainControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BindingResultConverter converter;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private PostDTOValidator validator;

    @InjectMocks
    private MainController mainController;

    User testUser;
    UserDTOOut userDTOOut;
    PostDTO postDTO;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.parse("08.02.1995",DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        testUser = new User("TestLogin","password","Иван",Gender.MALE,date,"testmail@gmail.com");
        userDTOOut = new UserDTOOut("login","name",30,Gender.MALE);
        postDTO = new PostDTO("Щука",new BigDecimal(10),"Test message");
    }


    @Test
    void getExistingUserProfile() {
        when(userService.findByLogin("login")).thenReturn(Optional.of(testUser));
        when(modelMapper.map(Optional.of(testUser).get(),UserDTOOut.class)).thenReturn(userDTOOut);

        UserDTOOut response = mainController.getUserProfile("login");

        assertEquals("login",userDTOOut.getLogin());
        assertEquals("name",userDTOOut.getName());
        assertEquals(30,userDTOOut.getAge());
        assertEquals(Gender.MALE,userDTOOut.getGender());
    }

    @Test
    void getNotExistingUserProfile() {
        when(userService.findByLogin("login")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,() -> mainController.getUserProfile("login"));
    }

    @Test
    void deleteUserProfile() {
        ResponseEntity<HttpStatus> response = mainController.deleteUserProfile("login");

        assertEquals(200,response.getStatusCode().value());
        verify(userService,times(1)).deleteUser("login");
    }

    @Test
    void addValidPost() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(postDTO,Post.class)).thenReturn(new Post());

        ResponseEntity<HttpStatus> response = mainController.addPost("login",postDTO,bindingResult);

        verify(userService,times(1)).addPost(eq("login"),any(Post.class));
        verify(modelMapper,times(1)).map(postDTO,Post.class);
        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void addInvalidPost() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(PostFieldsException.class,() -> mainController.addPost("login",postDTO,bindingResult));
    }

    @Test
    void deletePost() {
        ResponseEntity<HttpStatus> response = mainController.deletePost("login",1);

        assertEquals(200,response.getStatusCode().value());
        verify(userService,times(1)).deletePost("login",1);
    }

    @Test
    void UserNotFoundExceptionHandler() {
        UserNotFoundException exception = new UserNotFoundException();

        ResponseEntity<UserErrorResponse> response = mainController.exceptionHandler(exception);

        assertEquals(400,response.getStatusCode().value());
        assertEquals(response.getBody().getMessage(),exception.getMessage());
        assertTrue(response.getBody().getTimestamp() > 0);
    }

    @Test
    void PostFieldsExceptionHandler() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message","message Message");
        PostFieldsException exception = new PostFieldsException(map);

        ResponseEntity<HashMap<String, String>> response = mainController.exceptionHandler(exception);

        assertEquals(400,response.getStatusCode().value());
        assertEquals(response.getBody().get("message"),"message Message");
    }

    @Test
    void BadCredentialsExceptionHandler1() {
        BadCredentialsException exception = new BadCredentialsException("Bad credentials exception");

        ResponseEntity<PostErrorResponse> response = mainController.exceptionHandler(exception);

        assertEquals(400,response.getStatusCode().value());
        assertEquals(response.getBody().getMessage(),exception.getMessage());
        assertTrue(response.getBody().getTimestamp() > 0);
    }
}