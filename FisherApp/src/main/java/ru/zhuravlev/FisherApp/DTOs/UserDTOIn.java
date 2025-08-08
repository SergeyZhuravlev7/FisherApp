package ru.zhuravlev.FisherApp.DTOs;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import ru.zhuravlev.FisherApp.Models.Gender;

public class UserDTOIn {

    @NotNull
    @Size (min = 8, max = 30, message = "Длина логина должна быть от 8 до 30 символом")
    @NotBlank (message = "Логин не может состоять из пробелов.")
    private String login;

    @NotNull
    @Pattern (regexp = "[А-Яа-я]{3,15}", message = "Имя должно состоять из символов русского алфавита и быть длиной от 3 до 15 символов.")
    @NotBlank (message = "Имя не может состоять из пробелов.")
    private String name;

    @NotNull
    @NotBlank (message = "Пароль не может состоять из пробелов.")
    @Pattern (regexp = "[A-Za-z!?#$%*()]{8,}", message = "Пароль может состоять из символов латинского алфавита и спецсимволов !?#$%*(). и быть длиной не менее 8 символов")
    private String password;

    private String birthDate;

    @NotNull
    @Enumerated (EnumType.STRING)
    private Gender gender;

    @NotNull (message = "Email не может быть пустым.")
    @Email (message = "Email не соответствует формату.")
    private String email;

    public UserDTOIn() {
    }

    public UserDTOIn(String login,String name,String password,String birthDate,Gender gender,String email) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
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

    public Gender getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }
}
