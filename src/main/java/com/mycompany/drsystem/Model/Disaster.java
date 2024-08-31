/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.drsystem.Model;

import java.sql.Timestamp;

/**
 *
 * @author powertec
 */
public class Disaster {
    private int disasterId;
    private String disasterType;
    private String location;
    private String description;
    private int userId;
    private Timestamp createdDate;
    private String status;

    // Constructor, Getters, and Setters

    public Disaster(int disasterId, String disasterType, String location, String description, int userId, Timestamp createdDate, String status) {
        this.disasterId = disasterId;
        this.disasterType = disasterType;
        this.location = location;
        this.description = description;
        this.userId = userId;
        this.createdDate = createdDate;
        this.status = status;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}