package com.free.pubg.uc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.free.pubg.R;
import com.free.pubg.uc.models.Winner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.WinnerHolder> {

    private Context mContext;
    private ArrayList<Winner> dataList;

    public WinnerAdapter(Context mContext, ArrayList<Winner> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public WinnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.royal_pass_winner_item, parent, false);
        return new WinnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WinnerHolder holder, int position) {

        Winner winner = dataList.get(position);
        holder.date.setText(winner.getRequest_date());
        holder.name.setText(winner.getName());
        String strEmail = winner.getEmail();
        String[] emailParts = strEmail.split("@");
        holder.email.setText(strEmail.substring(0, 4) + "*********@" + emailParts[1]);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class WinnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.email)
        TextView email;

        public WinnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
