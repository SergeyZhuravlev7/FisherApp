package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.*;
import ru.zhuravlev.FisherApp.Models.Fish;

import java.math.BigDecimal;


public class PostDTO {

    private int id;

    @NotNull
    private String fish;

    private BigDecimal fish_weight;

    @NotNull(message = "Сообщение не должно быть пустым.")
    @Size(min = 1, max = 300, message = "Сообщение должно быть длиной от 1 до 300 символов.")
    private String message;

    public PostDTO() {
    }

    public PostDTO(String fish, BigDecimal fish_weight, String message) {
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

    public String getFish() {
        return fish;
    }

    public void setFish(String fish) {
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
