package ru.zhuravlev.FisherApp.DTOs;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import ru.zhuravlev.FisherApp.Models.Gender;

public class UserDTOIn {

    @NotNull
    @Size(min = 8, max = 30, message = "Длина логина должна быть от 8 до 30 символом")
    @NotBlank(message = "Логин не может состоять из пробелов.")
    private String login;

    @NotNull
    @Size(min = 3, max = 30, message = "Длина имени должна быть от 3 до 30 символов")
    @NotBlank(message = "Имя не может состоять из пробелов.")
    private String name;

    @NotNull
    @NotBlank(message = "Пароль не может состоять из пробелов.")
    @Pattern(regexp = "[A-Za-z!?#$%*()]{8,}", message = "Пароль может состоять из символов латинского алфавита и спецсимволов !?#$%*().")
    private String password;

    @Min(value = 12, message = "Пользователь должен быть старше 12 лет.")
    @Max(value = 100, message = "Пользователь должен быть моложе 100 лет")
    private int age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserDTOIn() {
    }

    public UserDTOIn(String login, String name, String password, int age, Gender gender) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "UserDTOIn{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
