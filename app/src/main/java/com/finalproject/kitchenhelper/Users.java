package com.finalproject.kitchenhelper;

public class Users {

    String name , email,type;

    public Users() {
        this.email = "";
        this.name = "";
        this.type = "employee";

    }

    public Users(String name, String ID,String type) {
        this.name = name;
        this.email = ID;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
