package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;


public class PostDTO {

    private FishDTO fish;

    @Min(value = 0, message = "Вес рыбы должен быть больше 0.")
    private double fish_weight;

    @Size(min = 1, max = 300, message = "Сообщение должено быть длиной от 1 до 300 символов.")
    private String message;

    public PostDTO() {
    }

    public PostDTO(FishDTO fish, int fish_weight, String message) {
        this.fish = fish;
        this.fish_weight = fish_weight;
        this.message = message;
    }

    public FishDTO getFish() {
        return fish;
    }

    public void setFish(FishDTO fish) {
        this.fish = fish;
    }

    public double getFish_weight() {
        return fish_weight;
    }

    public void setFish_weight(int fish_weight) {
        this.fish_weight = fish_weight;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
