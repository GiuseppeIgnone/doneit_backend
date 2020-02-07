package com.sini.doneit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "personal_card", uniqueConstraints = @UniqueConstraint(columnNames = {"users"}))

public class PersonalCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String faculty;
    private String university = "";
    private String telephone;
    private String statusDescription;
    private Boolean done = false;
    @Type(type = "text")
    private String base64StringImage;

    @OneToOne(optional = false)
    @JoinColumn(name = "users")
    @MapsId
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;


    public PersonalCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversity() {
        return university;
    }

    public PersonalCard setUniversity(String university) {
        this.university = university;
        return this;
    }

    public String getFaculty() {
        return faculty;
    }

    public PersonalCard setFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public String getTelephone() {
        return telephone;
    }

    public PersonalCard setTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public PersonalCard setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
        return this;
    }

    public Boolean getDone() {
        return done;
    }

    public PersonalCard setDone(Boolean done) {
        this.done = done;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PersonalCard setUser(User user) {
        this.user = user;
        return this;
    }

    public String getBase64StringImage() {
        return base64StringImage;
    }

    public PersonalCard setBase64StringImage(String base64StringImage) {
        this.base64StringImage = base64StringImage;
        return this;
    }


    public Wallet getWallet() {
        return wallet;
    }


    public PersonalCard setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    @Override
    public String toString() {
        return "PersonalCard{" +
                "id=" + id +
                ", faculty='" + faculty + '\'' +
                ", university='" + university + '\'' +
                ", telephone='" + telephone + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", done=" + done +
                ", base64StringImage='" + base64StringImage + '\'' +
                ", user=" + user +
                '}';
    }
}
