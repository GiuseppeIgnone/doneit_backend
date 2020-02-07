package com.sini.doneit.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;
    private String surname;
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Todo> todoList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Event> eventList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Proposal> proposals;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private PersonalCard personalCard;


    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Followers> followers;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Followers> following;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EventPartecipation> eventPartecipations;

    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MessageChat> messageReceived;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MessageChat> messageSent;


    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
        todo.setUser(this);
    }

    public List<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Followers> followers) {
        this.followers = followers;
    }

    public List<Followers> getFollowing() {
        return following;
    }

    public void setFollowing(List<Followers> following) {
        this.following = following;
    }

    public void removeTodo(Todo todo) {
        todoList.remove(todo);
        todo.setUser(null);
    }

    public void addFollower(Followers follower) {
        this.following.add(follower);
    }

    public void addFollowing(Followers follower) {
        this.followers.add(follower);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonalCard getPersonalCard() {
        return personalCard;
    }

    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }

    public boolean isOwnerOfTodo(Todo t) {
        return todoList.contains(t);
    }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}