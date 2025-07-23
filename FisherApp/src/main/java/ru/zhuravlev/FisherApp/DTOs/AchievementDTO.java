package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.Size;

public class AchievementDTO {

    @Size(max = 60, message = "Название достижения не должно превышать 60 символов")
    private String name;

    @Size(max = 100, message = "Описание достижения не должно превышать 100 символов")
    private String description;

    public AchievementDTO() {
    }

    public AchievementDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
