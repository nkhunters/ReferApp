package com.free.pubg.free.uc.models;

import java.util.ArrayList;

public class TransactionHistoryResponse {

    private Boolean error;
    private ArrayList<TransactionHistory> transaction_history;

    public TransactionHistoryResponse(Boolean error, ArrayList<TransactionHistory> transaction_history) {
        this.error = error;
        this.transaction_history = transaction_history;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<TransactionHistory> getTransaction_history() {
        return transaction_history;
    }
}
