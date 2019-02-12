package com.example.hi.model;

public class Chat {
    String chatId;
    String senderId;
    String receiverId;

    Chat(){}

    public Chat(String chatId){
        this.chatId = chatId;
    }

    public Chat(String chatId, String senderId, String receiverId) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
