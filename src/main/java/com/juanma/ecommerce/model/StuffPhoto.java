package com.juanma.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="stuff_photo")
public class StuffPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pid;
    private String photoPath;
    @ManyToOne
    @JoinColumn(name="stuff_id")
    @JsonIgnore() // Avoiding Recursive JSON response
    private Stuff stuff;

    public StuffPhoto(){}
    public StuffPhoto(String photoPath, Stuff stuff) {
        this.photoPath = photoPath;
        this.stuff = stuff;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Stuff getStuff() {
        return stuff;
    }

    public void setStuff(Stuff stuff) {
        this.stuff = stuff;
    }
}
