package ru.zhuravlev.FisherApp.DTOs;

import org.springframework.stereotype.Component;
import ru.zhuravlev.FisherApp.Models.Gender;

import java.util.List;

@Component
public class UserDTOOut {

    private String login;

    private String name;

    private int age;

    private Gender gender;

    private String email;

    private List<PostDTOOut> posts;

    private List<AchievementDTO> achievements;

    public UserDTOOut() {
    }

    public UserDTOOut(String login,String name,int age,Gender gender) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
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

    public List<PostDTOOut> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTOOut> posts) {
        this.posts = posts;
        this.posts.sort((p1,p2) -> p2.getId() - p1.getId());
    }

    public List<AchievementDTO> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementDTO> achievements) {
        this.achievements = achievements;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTOOut{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", posts=" + posts +
                ", achievements=" + achievements +
                '}';
    }
}
