package ru.zhuravlev.FisherApp.Models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

/* author-->
Sergey Zhuravlev
*/
@Entity
@Table (name = "achievements")
public class Achievement implements Comparable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Size (max = 60, message = "Название достижения не должно превышать 60 символов")
    private String name;

    @Size (max = 100, message = "Описание достижения не должно превышать 100 символов")
    private String description;

    @ManyToMany
    @JoinTable (name = "user_achievements", joinColumns = @JoinColumn (name = "achievement_id"), inverseJoinColumns = @JoinColumn (name = "user_id"))
    private List<User> users;

    public Achievement() {
    }

    public Achievement(String name,String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            Achievement achievement = (Achievement) o;
            return this.getId() - achievement.getId();
        }
        return 0;
    }
}
