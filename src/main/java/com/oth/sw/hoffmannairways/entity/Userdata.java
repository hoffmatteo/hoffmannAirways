package com.oth.sw.hoffmannairways.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Userdata {

    private String username;
    private String passwort;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Userdata(String username, String passwort) {
        this.username = username;
        this.passwort = passwort;
    }

    public Userdata() {

    }


}
