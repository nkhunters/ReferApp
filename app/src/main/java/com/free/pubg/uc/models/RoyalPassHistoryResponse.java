package com.free.pubg.uc.models;

import java.util.ArrayList;

public class RoyalPassHistoryResponse {

    private Boolean error;
    private ArrayList<Winner> royal_pass_history;

    public RoyalPassHistoryResponse(Boolean error, ArrayList<Winner> royal_pass_history) {
        this.error = error;
        this.royal_pass_history = royal_pass_history;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<Winner> getRoyal_pass_history() {
        return royal_pass_history;
    }
}
