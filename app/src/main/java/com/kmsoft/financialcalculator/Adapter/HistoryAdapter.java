package com.kmsoft.financialcalculator.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.financialcalculator.DetailsHistoryActivity;
import com.kmsoft.financialcalculator.HistoryFragment;
import com.kmsoft.financialcalculator.Model.EMI;
import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    HistoryFragment historyFragment;
    ArrayList<EMI> emiArrayList;

    public HistoryAdapter(HistoryFragment historyFragment, ArrayList<EMI> emiArrayList) {
        this.historyFragment = historyFragment;
        this.emiArrayList = emiArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        EMI emi = emiArrayList.get(position);

        DecimalFormat df = new DecimalFormat("#.##");

        holder.amount.setText(String.format("Amount - %s (%s%%)", df.format(emi.getPrincipalAmount()), df.format(emi.getInterestRate())));
        holder.month.setText(String.format("%s Month", df.format(emi.getLoanTenure())));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
        String dateString = dateFormat.format(new Date(Long.parseLong(emi.getDateTimeStamp())));
        holder.date.setText(dateString);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(historyFragment.getContext(), DetailsHistoryActivity.class);
            intent.putExtra("EMI", emi);
            historyFragment.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return emiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, month, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            month = itemView.findViewById(R.id.month);
            date = itemView.findViewById(R.id.date);
        }
    }
}
