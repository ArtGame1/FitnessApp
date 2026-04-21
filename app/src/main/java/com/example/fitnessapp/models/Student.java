package com.example.fitnessapp.models;

public class Student {
    private String fullName;
    private String groupId;

    public Student() {}
    public Student(String fullName, String groupId) {
        this.fullName = fullName;
        this.groupId = groupId;
    }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
}
