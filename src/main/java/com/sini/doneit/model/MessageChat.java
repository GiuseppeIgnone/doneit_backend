package com.sini.doneit.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MessageChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "from_user_fk")
    private User from;

    @ManyToOne()
    @JoinColumn(name = "to_user_fk")
    private User to;

    private String content;

    private Date date;

    public MessageChat() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "MessageChat{" +
                "id=" + id +
                ", from=" + from.getUsername() +
                ", to=" + to.getUsername() +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
