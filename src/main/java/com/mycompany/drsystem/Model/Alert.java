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
public class Alert {
    private int alertId;
    private int disasterId;
    private String alertMessage;
    private String sentByUsername;
    private Timestamp sentAt;
    private String status;

    // Constructor, Getters, and Setters

     public Alert(int alertId, int disasterId, String alertMessage, String sentByUsername, Timestamp sentAt, String status) {
        this.alertId = alertId;
        this.disasterId = disasterId;
        this.alertMessage = alertMessage;
        this.sentAt = sentAt;
        this.status = status;
        this.sentByUsername = sentByUsername;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
    
    public String getSentByUsername() {
        return sentByUsername;
    }

    public void setSentByUsername(String sentByUsername) {
        this.sentByUsername = sentByUsername;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
