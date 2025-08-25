package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table (name = "posts")
public class Post implements Comparable {

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

    private long likesCount = 0L;
    
    @Transient
    private boolean isLikedByThisUser;

//    @OneToMany (mappedBy = "post", cascade = CascadeType.ALL)
//    private final SortedSet<Comment> comments = new TreeSet<>();

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

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLikedByThisUser() {
        return isLikedByThisUser;
    }

    public void setLikedByThisUser(boolean likedByThisUser) {
        isLikedByThisUser = likedByThisUser;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user.getLogin() +
                ", fish='" + fish + '\'' +
                ", fishWeight=" + fishWeight +
                ", message='" + message + '\'' +
                ", created_at=" + created_at +
                ", likesCount=" + likesCount +
                ", isLikedByThisUser=" + isLikedByThisUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && Objects.equals(user,post.user) && Objects.equals(fish,post.fish) && Objects.equals(fishWeight,post.fishWeight) && Objects.equals(message,post.message) && Objects.equals(created_at,post.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,user,fish,fishWeight,message,created_at);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Post post) {
            return this.getId() - post.getId();
        } else return 0;
    }
}
