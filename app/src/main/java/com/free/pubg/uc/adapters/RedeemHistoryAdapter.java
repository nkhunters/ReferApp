package com.free.pubg.uc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.free.pubg.R;
import com.free.pubg.uc.models.RedeemHistory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedeemHistoryAdapter extends RecyclerView.Adapter<RedeemHistoryAdapter.RedeemHistoryViewHolder> {

    private ArrayList<RedeemHistory> dataList;

    public RedeemHistoryAdapter(ArrayList<RedeemHistory> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RedeemHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.redeem_history_item, parent, false);
        return new RedeemHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedeemHistoryViewHolder holder, int position) {

        RedeemHistory history = dataList.get(position);
        holder.type.setText(history.getType());
        holder.date.setText(history.getRequest_date());
        holder.mobile_id.setText(history.getMobile_pubg_id());
        holder.ucBtn.setText(history.getUc() + " UC");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class RedeemHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type)
        TextView type;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.uc_btn)
        Button ucBtn;

        @BindView(R.id.mobile_id)
        TextView mobile_id;

        public RedeemHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
