package com.kmsoft.financialcalculator.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.financialcalculator.DetailsHistoryActivity;
import com.kmsoft.financialcalculator.Model.DetailesEmi;
import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder> {

    DetailsHistoryActivity detailsHistoryActivity;
    ArrayList<DetailesEmi> detailesEmiArrayList;

    public HistoryDetailAdapter(DetailsHistoryActivity detailsHistoryActivity, ArrayList<DetailesEmi> detailesEmiArrayList) {
        this.detailsHistoryActivity = detailsHistoryActivity;
        this.detailesEmiArrayList = detailesEmiArrayList;
    }

    @NonNull
    @Override
    public HistoryDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_history_list, parent, false);
        return new HistoryDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailAdapter.ViewHolder holder, int position) {

        DetailesEmi detailesEmi = detailesEmiArrayList.get(position);

        DecimalFormat df = new DecimalFormat("#.##");

        int month = (int) detailesEmi.getMonth();
        holder.month.setText(String.valueOf(month));
        holder.principal.setText(df.format(detailesEmi.getPrincipal()));
        holder.interest.setText(df.format(detailesEmi.getInterest()));
        String value  = df.format(detailesEmi.getTotalBalance());
        holder.totalBalance.setText(extractNumericPart(value));
    }

    private String extractNumericPart(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    @Override
    public int getItemCount() {
        return detailesEmiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView month,principal,interest,totalBalance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month);
            principal = itemView.findViewById(R.id.principal);
            interest = itemView.findViewById(R.id.interest);
            totalBalance = itemView.findViewById(R.id.total_balance);
        }
    }
}
