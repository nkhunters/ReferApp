package com.free.pubg.free.uc.models;

import java.util.ArrayList;

public class RedeemHistoryResponse {

    private Boolean error;
    private ArrayList<RedeemHistory> redeem_history;

    public RedeemHistoryResponse(Boolean error, ArrayList<RedeemHistory> redeem_history) {
        this.error = error;
        this.redeem_history = redeem_history;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<RedeemHistory> getRedeem_history() {
        return redeem_history;
    }
}
