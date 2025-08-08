package ru.zhuravlev.FisherApp.Сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    private BindingResult bindingResult;

    private UserDTOIn testUser;
    private UserDTOOut testUserDTO;
    private PostDTO postDTO;


    @BeforeEach
    void setUp() {
        testUser = new UserDTOIn("testDevLogin","Иван","password","08.02.1995",Gender.MALE,"testmail@gmail.com");
        testUserDTO = new UserDTOOut("testDevLogin","Иван",30,Gender.MALE);
        postDTO = new PostDTO("Сом",new BigDecimal("12.5"),"ТестовоеСообщение");
    }

    @Test
    void fullIntegrityTest() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin(testUser.getLogin());
        loginDTO.setPassword(testUser.getPassword());

        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().is(200));

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

        mockMvc.perform(post("/api/users/" + testUser.getLogin() + "/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTO))
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));

        mockMvc.perform(delete("/api/users/" + testUser.getLogin() + "/posts/5")
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));

        mockMvc.perform(delete("/api/users/" + testUser.getLogin())
                        .header("Authorization","Bearer " + accessToken))
                .andExpect(status().is(200));

        TokenDTO token = new TokenDTO();
        token.setRefreshToken(refreshToken);

        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(token)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("accessToken").hasJsonPath());
    }
}
