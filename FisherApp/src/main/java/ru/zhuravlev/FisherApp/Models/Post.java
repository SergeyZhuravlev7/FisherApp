package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fish_id")
    private Fish fish;

    private int fish_weight;

    public Post() {
    }

    public Post(User user, Fish fish, int fish_weight) {
        this.user = user;
        this.fish = fish;
        this.fish_weight = fish_weight;
    }


}
