package com.free.pubg.uc.models;

import java.util.ArrayList;

public class WinnerResponse {

    private Boolean error;
    private ArrayList<Winner> winners;

    public WinnerResponse(Boolean error, ArrayList<Winner> winners) {
        this.error = error;
        this.winners = winners;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<Winner> getWinners() {
        return winners;
    }
}
