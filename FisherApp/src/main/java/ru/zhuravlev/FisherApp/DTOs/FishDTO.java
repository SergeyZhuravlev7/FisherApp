package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.Size;

public class FishDTO {

    @Size(max = 40, message = "Название рыбы не должено превышать 40 символов")
    private String name;

    public FishDTO() {
    }

    public FishDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
