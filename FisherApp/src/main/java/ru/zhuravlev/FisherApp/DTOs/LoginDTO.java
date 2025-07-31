package ru.zhuravlev.FisherApp.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginDTO {

    @NotNull
    @Size(min = 8, max = 30, message = "Длина логина должна быть от 8 до 30 символов")
    @NotBlank(message = "Логин не может состоять из пробелов.")
    private String login;

    @NotNull
    @NotBlank(message = "Пароль не может состоять из пробелов.")
    @Pattern(regexp = "[A-Za-z!?#$%*()]{8,}", message = "Пароль может состоять из символов латинского алфавита и спецсимволов !?#$%*().")
    private String password;

    public LoginDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
