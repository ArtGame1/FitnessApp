package com.example.fitnessapp.models;

public class NotificationItem {
    private String title;
    private String message;
    private String date;
    private boolean isVisible;

    public NotificationItem(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.isVisible = true;
    }

    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }
}
