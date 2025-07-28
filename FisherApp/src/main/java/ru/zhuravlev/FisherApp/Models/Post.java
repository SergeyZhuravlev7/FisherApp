package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "fish_id")
    private Fish fish;

    @Min(value = 0, message = "Вес рыбы должен быть больше 0.")
    @Column(precision = 4, scale = 2)
    private BigDecimal fish_weight;

    @Size(min = 1, max = 300, message = "Сообщение должено быть длиной от 1 до 300 символов.")
    private String message;

    public Post() {
    }

    public Post(User user, Fish fish, BigDecimal fish_weight) {
        this.user = user;
        this.fish = fish;
        this.fish_weight = fish_weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
        fish.addPost(this);
    }

    public BigDecimal getFish_weight() {
        return fish_weight;
    }

    public void setFish_weight(BigDecimal fish_weight) {
        this.fish_weight = fish_weight;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", fish=" + fish +
                ", fish_weight=" + fish_weight +
                ", message='" + message + '\'' +
                '}';
    }
}
