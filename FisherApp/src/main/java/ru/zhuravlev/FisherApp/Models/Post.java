package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "posts")
public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    private String fish;

    @Column (precision = 4, scale = 2)
    private BigDecimal fishWeight;

    @NotNull (message = "Сообщение не должно быть пустым.")
    @Size (min = 1, max = 300, message = "Сообщение должно быть длиной от 1 до 300 символов.")
    private String message;

    private LocalDateTime created_at;

    public Post() {
        this.created_at = LocalDateTime.now();
    }

    public Post(User user,String fish,BigDecimal fishWeight) {
        this.user = user;
        this.fish = fish;
        this.fishWeight = fishWeight;
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

    public String getFish() {
        return fish;
    }

    public void setFish(String fish) {
        this.fish = fish;
    }

    public BigDecimal getFishWeight() {
        return fishWeight;
    }

    public void setFishWeight(BigDecimal fishWeight) {
        this.fishWeight = fishWeight;
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
                ", fish_weight=" + fishWeight +
                ", message='" + message + '\'' +
                '}';
    }
}
