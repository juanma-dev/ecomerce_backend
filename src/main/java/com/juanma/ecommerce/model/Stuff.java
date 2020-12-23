package com.juanma.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Stuff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sid;
    private String sname;
    private String category;
    private double price;
    @ManyToOne
    @JoinColumn(name="user_uid")
    @JsonIgnore() // Avoiding Recursive JSON response
    private User user;
    @OneToMany(mappedBy = "stuff", fetch=FetchType.EAGER)
    List<StuffPhoto> photos;

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<StuffPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<StuffPhoto> photos) {
        this.photos = photos;
    }
}
