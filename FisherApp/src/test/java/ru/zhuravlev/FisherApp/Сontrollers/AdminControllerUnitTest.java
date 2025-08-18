package ru.zhuravlev.FisherApp.Сontrollers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.zhuravlev.FisherApp.Services.PostService;
import ru.zhuravlev.FisherApp.Services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith (MockitoExtension.class)
class AdminControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void deleteUser() {
        ResponseEntity<HttpStatus> response = adminController.deleteUser("testlogin");
        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void deletePostWithValidId() {
        ResponseEntity<HttpStatus> response = adminController.deletePost("2");

        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void deletePostWithInvalidId() {
        ResponseEntity<HttpStatus> response = adminController.deletePost("invalid");

        assertEquals(400,response.getStatusCode().value());
    }

}