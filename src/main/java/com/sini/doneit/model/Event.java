package com.sini.doneit.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Date date;
    private String place;
    private String placeId;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<EventPartecipation> eventPartecipations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<EventPartecipation> getEventPartecipations() {
        return eventPartecipations;
    }

    public void setEventPartecipations(List<EventPartecipation> eventPartecipations) {
        this.eventPartecipations = eventPartecipations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

//    @Override
//    public String toString() {
//        return "Event{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", description='" + description + '\'' +
//                ", date=" + date +
//                ", place='" + place + '\'' +
//                ", placeId='" + placeId + '\'';
//    }
}
