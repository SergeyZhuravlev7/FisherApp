package ru.zhuravlev.FisherApp.Сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.zhuravlev.FisherApp.DTOs.FishDTO;
import ru.zhuravlev.FisherApp.DTOs.PostDTO;
import ru.zhuravlev.FisherApp.DTOs.UserDTOOut;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;
import ru.zhuravlev.FisherApp.Util.BindingResultConverter;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModelMapper modelMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BindingResultConverter bindingResultConverter;

    @MockitoBean
    private BindingResult bindingResult;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private UserDTOOut testUserDTO;
    private PostDTO validPostDTO;
    private PostDTO invalidPostDTO;

    @BeforeEach
    void setUp() {
        testUser = new User("loginnnn", "password", "TestName", 30, Gender.MALE);
        testUserDTO = new UserDTOOut("loginnnn", "TestName", 30, Gender.MALE);
        validPostDTO = new PostDTO(new FishDTO("Щука"), BigDecimal.valueOf(18), "Тестовое сообщение");
        invalidPostDTO = new PostDTO();
    }

    @Test
    void getUserProfileWithExistingUser() throws Exception {
        when(userService.findByLogin(testUser.getLogin())).thenReturn(Optional.of(testUser));
        when(modelMapper.map(testUser, UserDTOOut.class)).thenReturn(testUserDTO);
        mockMvc.perform(get("/api/users/loginnnn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("login").value("loginnnn"))
                .andExpect(jsonPath("name").value("TestName"))
                .andExpect(jsonPath("age").value("30"));
    }

    @Test
    void getUserProfileWithNotExistingUser() throws Exception {
        String notExistingLogin = "NotExistingLogin";
        when(userService.findByLogin(notExistingLogin)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/users/" + notExistingLogin).with(anonymous()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("message").value("Пользователь с таким логином не найден."));
    }

    @Test
    @WithMockUser(username = "loginnnn")
    void deleteUserProfileWithAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn")
                        .content(objectMapper.writeValueAsString(validPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @WithAnonymousUser
    @Test
    void deleteUserProfileWithoutAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn"))
                .andExpect(status().is(401));
    }

    @WithMockUser(username = "loginnnn1")
    @Test
    void deleteUserProfileWithAnotherAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn"))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    void addInvalidPostWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts").
                        content(objectMapper.writeValueAsString(invalidPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "loginnnn1")
    void addValidPostWithAnotherAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts").
                        content(objectMapper.writeValueAsString(validPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "loginnnn1")
    void addInvalidPostWithAnotherAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts")
                        .content(objectMapper.writeValueAsString(invalidPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    void addValidPostWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts")
                        .content(objectMapper.writeValueAsString(validPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "loginnnn")
    void addValidPostWithAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts")
                        .content(objectMapper.writeValueAsString(validPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "loginnnn")
    void addInvalidPostWithAuth() throws Exception {
        mockMvc.perform(post("/api/users/loginnnn/posts")
                        .content(objectMapper.writeValueAsString(invalidPostDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").hasJsonPath())
                .andExpect(jsonPath("timestamp").hasJsonPath());
    }

    @Test
    @WithMockUser(username = "loginnnn")
    void deletePostWithAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn/posts/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void deletePostWithoutAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn/posts/1"))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "loginnnn")
    void deletePostWithAnotherAuth() throws Exception {
        mockMvc.perform(delete("/api/users/loginnnn1234/posts/1"))
                .andExpect(status().is(403));
    }
}