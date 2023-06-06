package com.example.testfinder.item;

public class Item {
    String name;
    String description;
    String subject;
    String image1;
    String image2;
    String image3;
    String recognized_text;
    int grade;
    long user_id;
    int school;

    public Item(String name, String description,String subject, String image1, String image2, String image3, int grade, String recognized_text, long user_id, int school) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.grade = grade;
        this.recognized_text = recognized_text;
        this.user_id = user_id;
        this.school = school;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getRecognized_text() {
        return recognized_text;
    }

    public void setRecognized_text(String recognized_text) {
        this.recognized_text = recognized_text;
    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }
}
