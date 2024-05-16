package com.Aswat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table( name= "aswat")
@Data

public class Category {
    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long id;

    private String Category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
