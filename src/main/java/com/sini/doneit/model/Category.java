package com.sini.doneit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer cfuPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Todo> todoList;

    public Category() {
    }

    public Category(String name, Integer cfuPrice){
        this.name = name;
        this.cfuPrice = cfuPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCfuPrice() {
        return cfuPrice;
    }

    public void setCfuPrice(Integer cfuPrice) {
        this.cfuPrice = cfuPrice;
    }

    public void addTodo(Todo todo){
        this.todoList.add(todo);
        todo.setCategory(this);
    }

    public void removeTodo(Todo todo){
        this.todoList.remove(todo);
        todo.setCategory(null);
    }

}
