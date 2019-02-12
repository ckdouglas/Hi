package com.example.hi.model;

import java.util.ArrayList;

public class Message {

    String recieverId;
    String message;
    String senderId;
    String time_sent;

    public String getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(String time_sent) {
        this.time_sent = time_sent;
    }

    public Message(String senderId, String recieverId, String message ) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.message = message;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    Message(){}

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

}
