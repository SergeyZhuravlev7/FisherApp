package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public class PostDTO {

    private int id;

    private String fish;

    private BigDecimal fishWeight;

    @NotNull (message = "Сообщение не должно быть пустым.")
    @Size (min = 1, max = 300, message = "Сообщение должно быть длиной от 1 до 300 символов.")
    private String message;

    public PostDTO() {
    }

    public PostDTO(String fish,BigDecimal fishWeight,String message) {
        this.fish = fish;
        this.fishWeight = fishWeight;
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

    public BigDecimal getFishWeight() {
        return fishWeight;
    }

    public void setFishWeight(BigDecimal fishWeight) {
        this.fishWeight = fishWeight;
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
                ", fish_weight=" + fishWeight +
                ", message='" + message + '\'' +
                '}';
    }
}
