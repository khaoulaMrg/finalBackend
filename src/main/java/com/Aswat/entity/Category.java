package com.Aswat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table( name= "categ")
@Data

public class Category {
    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long id;

    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
