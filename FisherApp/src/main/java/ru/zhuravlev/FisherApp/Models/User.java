package ru.zhuravlev.FisherApp.Models;

/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 8, max = 30, message = "Длина логина должна быть от 8 до 30 символом")
    private String login;

    @NotNull
    private String password_hash;

    @NotNull
    @Size(min = 3, max = 30, message = "Длина имени должна быть от 3 до 30 символов")
    private String name;

    @Min(value = 12, message = "Пользователь должен быть старше 12 лет.")
    @Max(value = 100, message = "Пользователь должен быть моложе 100 лет")
    private int age;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private long created_at;

    public User() {
    }

    public User(String login, String password_hash, String name, int age, Gender gender) {
        this.login = login;
        this.password_hash = password_hash;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.created_at = System.currentTimeMillis();
    }

}
