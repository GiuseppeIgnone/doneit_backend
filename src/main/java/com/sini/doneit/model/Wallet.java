package com.sini.doneit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cfu = 15;

    @JsonIgnore
    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY, optional = false)
    private PersonalCard personalCard;

    public Wallet() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean removeCfu(Integer value) {
        if (this.cfu >= value) {
            this.cfu -= value;
            return true;
        } else {
            return false;
        }
    }

    public void addCfu(Integer value) {
        this.cfu += value;
    }

    public Integer getCfu() {
        return cfu;
    }

    public void setCfu(Integer cfu) {
        this.cfu = cfu;
    }

    public PersonalCard getPersonalCard() {
        return personalCard;
    }

    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", cfu=" + cfu +
                '}';
    }
}
