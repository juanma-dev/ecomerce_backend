package com.juanma.ecommerce.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long aid;
    private String aname;
    @ManyToMany
    private List<User> users;

    public Authority() {}
    public Authority(long aid, String aname, List<User> users) {
        this.aid = aid;
        this.aname = aname;
        this.users = users;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
