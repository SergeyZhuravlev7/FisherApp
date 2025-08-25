package ru.zhuravlev.FisherApp.Сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.zhuravlev.FisherApp.DTOs.*;
import ru.zhuravlev.FisherApp.Models.Gender;
import ru.zhuravlev.FisherApp.Validators.PostDTOValidator;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FullIntegrityTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private MainController mainController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostDTOValidator postDTOValidator;

    @MockitoBean
    private BindingResult bindingResult;

    private UserDTORegistration testUser;
    private UserDTOOut testUserDTO;
    private PostDTO postDTO;
    private UserDTOFilling userDTOFilling;


    @BeforeEach
    void setUp() {
        testUser = new UserDTORegistration("testDevLogin","password","testmail@gmail.com");
        testUserDTO = new UserDTOOut("testDevLogin","Иван",30,Gender.MALE);
        postDTO = new PostDTO("Сом",new BigDecimal("12.5"),"ТестовоеСообщение");
        userDTOFilling = new UserDTOFilling("Иван","08.02.1995","МужСКой");
    }


    @Test
    void fullIntegrityTest() throws Exception {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(testUser.getLogin());
        loginDTO.setPassword(testUser.getPassword());

        //регистрация пользователя
        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().is(200));

        //получение профиля юзера
        mockMvc.perform(get("/api/users/" + testUser.getLogin()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("login").value("testDevLogin"))
                .andExpect(jsonPath("email").value("testmail@gmail.com"))
                .andExpect(jsonPath("age").value(0))
                .andExpect(jsonPath("gender").isEmpty());

        //получение токенов доступа
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("accessToken").hasJsonPath())
                .andExpect(jsonPath("refreshToken").hasJsonPath());

        ResponseEntity<Map<String, String>> response = authController.login(loginDTO,bindingResult);

        Assertions.assertNotNull(response.getBody());
        String accessToken = response.getBody().get("accessToken");
        String refreshToken = response.getBody().get("refreshToken");

        //добавление даты рождения, имени, пола
        mockMvc.perform(patch("/api/users/" + testUser.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOFilling))
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk());

        //получение профиля с новыми данными, посты отсутствуют
        mockMvc.perform(get("/api/users/" + testUser.getLogin()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("login").value("testDevLogin"))
                .andExpect(jsonPath("email").value("testmail@gmail.com"))
                .andExpect(jsonPath("age").value(30))
                .andExpect(jsonPath("gender").isNotEmpty());


        when(bindingResult.hasErrors()).thenReturn(false);
        // добавляем новый пост
        mockMvc.perform(post("/api/users/" + testUser.getLogin() + "/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTO))
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));

        //получение профиля с новыми данными, пост добавлен
        String result = mockMvc.perform(get("/api/users/" + testUser.getLogin()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("login").hasJsonPath())
                .andExpect(jsonPath("email").hasJsonPath())
                .andExpect(jsonPath("age").value(30))
                .andExpect(jsonPath("gender").isNotEmpty())
                .andExpect(jsonPath("posts").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);

        //лайкаем добавленный пост
        mockMvc.perform(post("/api/users/" + testUser.getLogin() + "/posts/5/like")
                        .with(user("testDevLogin").roles("USER")))
                .andExpect(status().is(200));

        //получение профиля с новыми данными, пост добавлен и поставлен лайк
        String resultAfterLike = mockMvc.perform(get("/api/users/" + testUser.getLogin())
                        .with(user("testDevLogin").roles("USER")))
                .andExpect(status().is(200))
                .andExpect(jsonPath("login").hasJsonPath())
                .andExpect(jsonPath("email").hasJsonPath())
                .andExpect(jsonPath("age").value(30))
                .andExpect(jsonPath("gender").isNotEmpty())
                .andExpect(jsonPath("posts").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        System.out.println(resultAfterLike);

        assertNotEquals(result,resultAfterLike);

        // удаляем добавленный пост
        mockMvc.perform(delete("/api/users/" + testUser.getLogin() + "/posts/5")
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));

        TokenDTO token = new TokenDTO();
        token.setRefreshToken(refreshToken);

        // обновляем токен доступа
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(token)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("accessToken").hasJsonPath());

        // удаление пользователя
        mockMvc.perform(delete("/api/users/" + testUser.getLogin())
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));
    }
}
