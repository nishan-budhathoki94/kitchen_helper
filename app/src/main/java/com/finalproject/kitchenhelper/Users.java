package com.finalproject.kitchenhelper;

public class Users {

    String name , email;

    public Users() {
        this.email = "";
        this.name = "";
    }

    public Users(String name, String ID) {
        this.name = name;
        this.email = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
