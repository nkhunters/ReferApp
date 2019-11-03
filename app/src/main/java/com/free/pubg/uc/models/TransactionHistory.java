package com.free.pubg.uc.models;

public class TransactionHistory {

    private String id, bonus_type, bonus_date, uc;

    public TransactionHistory(String id, String bonus_type, String bonus_date, String uc) {
        this.id = id;
        this.bonus_type = bonus_type;
        this.bonus_date = bonus_date;
        this.uc = uc;
    }

    public String getId() {
        return id;
    }

    public String getBonus_type() {
        return bonus_type;
    }

    public String getBonus_date() {
        return bonus_date;
    }

    public String getUc() {
        return uc;
    }
}
