package com.free.pubg.free.uc.models;

public class Winner {

    private String id, user_id, name, email, request_date;

    public Winner(String id, String user_id, String name, String email, String request_date) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.request_date = request_date;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRequest_date() {
        return request_date;
    }
}
