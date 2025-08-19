package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTORegistration {

    @NotNull (message = "Логин не может быть пустым.")
    @Pattern (regexp = "^[A-Za-z0-9]{8,20}$", message = "Логин может состоять из символов латинского алфавита и цифр от 0 до 9. и должен быть длиной от 8 до 20 символов.")
    private String login;

    @NotNull (message = "Пароль не может быть пустым.")
    @Pattern (regexp = "^[A-Za-z!?#$%*()]{8,30}$", message = "Пароль может состоять из символов латинского алфавита и спецсимволов !?#$%*(). и быть длиной от 8 до 30 символов.")
    private String password;

    @NotNull (message = "Email не может быть пустым.")
    @Email (message = "Email не соответствует формату.")
    @Size (max = 30, message = "Email должен быть длиной до 30 символов.")
    private String email;

    public UserDTORegistration() {
    }

    public UserDTORegistration(String login,String password,String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
