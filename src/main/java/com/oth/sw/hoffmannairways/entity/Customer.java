package com.oth.sw.hoffmannairways.entity;

import javax.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerID;
    private String adress;
    private String name;
    private String surname;
    @Embedded
    private Userdata userdata;

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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


    public Customer(String adress, String name, String surname, Userdata userdata) {
        this.adress = adress;
        this.name = name;
        this.surname = surname;
        this.userdata = userdata;
    }

    public Customer() {

    }
}
