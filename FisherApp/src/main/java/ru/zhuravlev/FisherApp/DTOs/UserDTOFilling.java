package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserDTOFilling {

    @NotNull (message = "Имя не может быть пустым.")
    @Pattern (regexp = "^[А-Я][а-я]{2,14}$", message = "Имя должно состоять из символов русского алфавита и быть длиной от 3 до 15 символов.")
    private String name;

    private String birthDate;

    private String gender;

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public UserDTOFilling() {
    }

    public UserDTOFilling(String name,String birthDate,String gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }
}
