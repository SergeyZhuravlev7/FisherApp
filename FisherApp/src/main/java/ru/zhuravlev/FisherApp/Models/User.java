package ru.zhuravlev.FisherApp.Models;

/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 8, max = 30, message = "Длина логина должна быть от 8 до 30 символов.")
    @NotBlank
    private String login;

    @Column(name = "password_hash")
    private String password;

    @NotNull
    @Size(min = 3, max = 30, message = "Длина имени должна быть от 3 до 30 символов.")
    private String name;

    @Min(value = 12, message = "Возраст должен быть больше 12 лет.")
    @Max(value = 100, message = "Возраст должен быть меньше 100 лет.")
    private int age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Post> posts;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Achievement> achievements;

    private LocalDateTime created_at;

    private String role;

    public User() {
        this.created_at = LocalDateTime.now();
    }

    public User(String login,String password,String name,int age,Gender gender) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", posts=" + posts +
                ", achievements=" + achievements +
                ", created_at=" + created_at +
                ", role='" + role + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void addPost(Post post) {
        if (this.posts == null) this.posts = new ArrayList<>();
        this.posts.add(post);
        post.setUser(this);
    }

    public void deletePost(Post post) {
        if (this.posts != null) this.posts.remove(post);
    }
}
