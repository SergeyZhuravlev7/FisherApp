package ru.zhuravlev.FisherApp.Сontrollers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FishControllerIntegrityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getFishWithAuth() throws Exception {
        mockMvc.perform(get("/api/fish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("names").isArray());
    }

    @Test
    @WithAnonymousUser
    void getFishWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/fish"))
                .andExpect(status().is4xxClientError());
    }
}