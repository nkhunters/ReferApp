package com.free.pubg.uc.models;

public class User {

    private String id, uc, refer_id, message;

    public User(String id, String uc, String refer_id, String message) {
        this.id = id;
        this.uc = uc;
        this.refer_id = refer_id;
        this.message = message;
    }

    public String getRefer_id() {
        return refer_id;
    }

    public String getId() {
        return id;
    }

    public String getUc() {
        return uc;
    }

    public String getMessage() {
        return message;
    }
}
