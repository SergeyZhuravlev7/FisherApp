package ru.zhuravlev.FisherApp.Models;

/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    @Column (name = "password_hash")
    private String password;

    private String name;

    @Transient
    private int age;

    @Enumerated (EnumType.STRING)
    private Gender gender;

    @Column (name = "birthdate")
    private LocalDate birthDate;

    private String email;

    @OneToMany (mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @OrderBy (value = "id DESC")
    private Set<Post> posts;

    @ManyToMany (mappedBy = "users", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    @OrderBy (value = "id ASC")
    private Set<Achievement> achievements;

    private LocalDateTime created_at;

    private String role;

    public User() {
        this.created_at = LocalDateTime.now();
        this.role = "USER";
    }

    public User(String login,String password,String name,Gender gender,LocalDate birthDate,String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.age = this.birthDate.until(LocalDate.now()).getYears();
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
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
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
        if (this.birthDate == null) return 0;
        return this.birthDate.until(LocalDate.now()).getYears();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
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
        if (this.posts == null) this.posts = new HashSet<>();
        this.posts.add(post);
        post.setUser(this);
    }

    public void deletePost(Post post) {
        if (this.posts != null) this.posts.remove(post);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = LocalDate.parse(birthDate,DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        this.age = this.birthDate.until(LocalDate.now()).getYears();
    }
}
