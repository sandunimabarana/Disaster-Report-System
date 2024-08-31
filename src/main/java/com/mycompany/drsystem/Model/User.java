/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Model;

/**
 *
 * @author powertec
 */
public class User {
    private int id;           // Unique identifier for the user
    private String username;  // Username of the user
    private String clientName;
    private String userType;  // User type (e.g., User, Administrator)

    // Constructor
    public User(int id, String username, String clientName, String userType) {
        this.id = id;
        this.username = username;
        this.clientName = clientName;
        this.userType = userType;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for clientName
    public String getClientName() {
        return clientName;
    }

    // Setter for clientName
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    // Getter for userType
    public String getUserType() {
        return userType;
    }

    // Setter for userType
    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return username; // Display the username in the ComboBox
    }
}