package com.free.pubg.free.uc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.free.pubg.R;
import com.free.pubg.free.uc.models.TransactionHistory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {

    private ArrayList<TransactionHistory> dataList;

    public TransactionHistoryAdapter(ArrayList<TransactionHistory> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction_history_item, parent, false);
        return new TransactionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {

        TransactionHistory history = dataList.get(position);
        holder.bonusType.setText(history.getBonus_type());
        holder.bonusDate.setText(history.getBonus_date());
        holder.ucBtn.setText(history.getUc() + " UC");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bonus_type)
        TextView bonusType;

        @BindView(R.id.bonus_date)
        TextView bonusDate;

        @BindView(R.id.uc_btn)
        Button ucBtn;

        public TransactionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
