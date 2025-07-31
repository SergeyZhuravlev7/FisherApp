package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public class PostDTO {

    private int id;

    @NotNull
    private FishDTO fish;

    @Min(value = 0, message = "Вес рыбы должен быть больше 0.")
    private BigDecimal fish_weight;

    @Size(min = 1, max = 300, message = "Сообщение должено быть длиной от 1 до 300 символов.")
    private String message;

    public PostDTO() {
    }

    public PostDTO(FishDTO fish, BigDecimal fish_weight, String message) {
        this.fish = fish;
        this.fish_weight = fish_weight;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FishDTO getFish() {
        return fish;
    }

    public void setFish(FishDTO fish) {
        this.fish = fish;
    }

    public BigDecimal getFish_weight() {
        return fish_weight;
    }

    public void setFish_weight(BigDecimal fish_weight) {
        this.fish_weight = fish_weight;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "fish=" + fish +
                ", fish_weight=" + fish_weight +
                ", message='" + message + '\'' +
                '}';
    }
}
