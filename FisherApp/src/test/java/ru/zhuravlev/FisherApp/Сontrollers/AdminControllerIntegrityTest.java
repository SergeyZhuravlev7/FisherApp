package ru.zhuravlev.FisherApp.Сontrollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.Post;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.PostService;
import ru.zhuravlev.FisherApp.Services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrityTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PostService postService;

    User user;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.parse("08.02.1995",DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        user = new User("loginnnn","password","Иван",Gender.MALE,date,"testmail@gmail.com");
    }

    @Test
    @WithMockUser (authorities = {"ADMIN"})
    void deleteUserWithAdmin() throws Exception {
        when(userService.findByLogin("login")).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/api/admin/users/login"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser
    void deleteUserWithUser() throws Exception {
        when(userService.findByLogin("login")).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/api/admin/users/login"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser (authorities = {"ADMIN"})
    void deletePostWithAdminAndValidId() throws Exception {
        when(postService.findById(1)).thenReturn(Optional.of(new Post()));

        mockMvc.perform(delete("/api/admin/posts/1"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser (authorities = {"ADMIN"})
    void deletePostWithAdminAndInvalidId() throws Exception {
        mockMvc.perform(delete("/api/admin/posts/invalidId"))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser
    void deletePostWithUser() throws Exception {
        mockMvc.perform(delete("/api/admin/posts/invalidId"))
                .andExpect(status().is(403));
    }
}