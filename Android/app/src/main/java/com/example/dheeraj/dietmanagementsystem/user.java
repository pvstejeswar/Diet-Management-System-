package com.example.dheeraj.dietmanagementsystem;

/**
 * Created by Dheeraj on 10/14/2015.
 */
public class user {
    String name, username, password;
    int age;

    public user(String name, int age, String username, String password){
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public user(String username, String password){
        this.username = username;
        this.password = password;
        this.age = -1;
        this.name = "";
    }
}
