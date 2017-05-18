package com.example.jerrychen.canteenapp;

import java.io.Serializable;

/**
 * Created by jerrychen on 4/7/17.
 */

public class User implements Serializable {
    private int id;
    private String email ,firstname,lastname,password;

    public User(int id, String email, String firstname, String lastname, String password) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }
}
