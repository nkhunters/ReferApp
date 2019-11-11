package com.free.pubg.free.uc.models;

public class RedeemHistory {

    private String id, type, uc, mobile_pubg_id, request_date;

    public RedeemHistory(String id, String type, String uc, String mobile_pubg_id, String request_date) {
        this.id = id;
        this.type = type;
        this.uc = uc;
        this.mobile_pubg_id = mobile_pubg_id;
        this.request_date = request_date;
    }

    public String getMobile_pubg_id() {
        return mobile_pubg_id;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUc() {
        return uc;
    }

    public String getRequest_date() {
        return request_date;
    }
}
