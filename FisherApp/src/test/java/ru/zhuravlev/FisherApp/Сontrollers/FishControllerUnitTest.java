package ru.zhuravlev.FisherApp.Сontrollers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.zhuravlev.FisherApp.Models.Fish;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
class FishControllerUnitTest {

    MockedStatic<Fish> mockedStatic = mockStatic(Fish.class);

    @InjectMocks
    private FishController fishController;

    @Test
    void getFish() {
        when(Fish.getFishNames()).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, List<String>>> response = fishController.getFish();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("names"));
    }
}