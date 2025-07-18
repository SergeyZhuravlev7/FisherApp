package ru.zhuravlev.FisherApp.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

/* author-->
Sergey Zhuravlev
*/
@Entity
public class Achievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 60, message = "Название достижения не должно превышать 60 символов")
    private String name;

    @Size(max = 100, message = "Описание достижения не должно превышать 100 символов")
    private String description;

    public Achievements() {
    }

    public Achievements(String name, String description) {
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
}
