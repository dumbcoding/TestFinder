package com.example.testfinder.comment;

public class Comment {
    Long id;
    Long user_id;
    String user_name;
    Long item_id;
    String text;

    public Comment(Long id, Long user_id, String user_name, Long item_id, String text) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.item_id = item_id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}