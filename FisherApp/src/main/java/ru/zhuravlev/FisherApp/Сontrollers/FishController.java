package ru.zhuravlev.FisherApp.Сontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhuravlev.FisherApp.Models.Fish;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/fish")
public class FishController {

    @GetMapping
    @PreAuthorize ("authentication != null")
    public ResponseEntity<Map<String, List<String>>> getFish() {
        return new ResponseEntity<>(Map.of("names",Fish.getFishNames()),HttpStatus.OK);
    }
}
