package ru.zhuravlev.FisherApp.Models;


/* author--> 
Sergey Zhuravlev
*/

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 40, message = "Название рыбы не должено превышать 40 символов")
    private String name;

    @OneToMany(mappedBy = "fish")
    private List<Post> posts;

    public Fish() {
    }

    public Fish(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
