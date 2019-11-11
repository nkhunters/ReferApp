package com.free.pubg.free.uc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.free.pubg.R;
import com.free.pubg.free.uc.models.Winner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoyalPassHistoryAdapter extends RecyclerView.Adapter<RoyalPassHistoryAdapter.RoyalPassHistoryViewHolder> {
    private Context mContext;
    private ArrayList<Winner> dataList;


    public RoyalPassHistoryAdapter(Context mContext, ArrayList<Winner> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RoyalPassHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.royal_pass_history_item, parent, false);
        return new RoyalPassHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoyalPassHistoryViewHolder holder, int position) {

        Winner winner = dataList.get(position);
        holder.name.setText(winner.getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class RoyalPassHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        public RoyalPassHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
