package com.sini.doneit.model;

import javax.persistence.*;

@Entity
@Table(name = "followers", uniqueConstraints = @UniqueConstraint(columnNames = {"from_user_fk", "to_user_fk"}))
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "from_user_fk")
    private User from;

    @ManyToOne()
    @JoinColumn(name = "to_user_fk")
    private User to;

    public Followers() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public Followers(User from, User to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Followers{" +
                "id=" + id +
                ", from=" + from.getUsername() +
                ", to=" + to.getUsername() +
                '}';
    }
}
