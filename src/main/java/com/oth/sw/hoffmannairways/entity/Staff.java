package com.oth.sw.hoffmannairways.entity;

import javax.persistence.*;

@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int staffId;
    private String name;
    private String surname;
    @Embedded
    private Userdata userdata;

    public int getStaffId() {
        return staffId;
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

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }

    public Staff(String name, String surname, Userdata userdata) {
        this.name = name;
        this.surname = surname;
        this.userdata = userdata;
    }

    public Staff() {

    }
}
