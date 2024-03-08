package com.kmsoft.financialcalculator.Fragment;

import static com.kmsoft.financialcalculator.MainActivity.isStep;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.Adapter.HistoryAdapter;
import com.kmsoft.financialcalculator.DBHelper;
import com.kmsoft.financialcalculator.MainActivity;
import com.kmsoft.financialcalculator.Model.EMI;
import com.kmsoft.financialcalculator.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    ImageView back, delete;
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    TextView noAnyHistory;
    DBHelper dbHelper;
    EMI emi;
    ArrayList<EMI> emiArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        init(view);

        dbHelper = new DBHelper(getContext());

        Display();

        back.setOnClickListener(v -> {
            isStep = true;
            Intent intent = new Intent(HistoryFragment.this.getContext(), MainActivity.class);
            startActivity(intent);
        });

        delete.setOnClickListener(v -> showDeleteBottomDialog());

        return view;
    }

    private void showDeleteBottomDialog() {
        Dialog dialog = new Dialog(getContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.delete_dailog_layout);
        dialog.setCancelable(false);
        dialog.show();

        TextView no = dialog.findViewById(R.id.canceldialog);
        TextView yes = dialog.findViewById(R.id.movetobin);

        no.setOnClickListener(v -> dialog.dismiss());

        yes.setOnClickListener(v -> {
            if (!emiArrayList.isEmpty()) {
                dbHelper.deleteData();
                emiArrayList.clear();
                historyAdapter.notifyDataSetChanged();
                noAnyHistory.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "No any data for delete", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
    }

    private void Display() {
        try {
            Cursor cursor = dbHelper.getAllData();
            if (cursor != null && cursor.moveToFirst()) {
                emiArrayList = new ArrayList<>();
                do {
                    int id = cursor.getInt(0);
                    double principalAmount = cursor.getDouble(1);
                    double interestRate = cursor.getDouble(2);
                    double loanTenure = cursor.getDouble(3);
                    double totalInterestAmount = cursor.getDouble(4);
                    double monthlyEmi = cursor.getDouble(5);
                    double totalPayment = cursor.getDouble(6);
                    String date = cursor.getString(7);

                    emi = new EMI(id, principalAmount, interestRate, loanTenure, totalInterestAmount, monthlyEmi, totalPayment, date);
                    emiArrayList.add(emi);
                } while (cursor.moveToNext());
                noAnyHistory.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                historyAdapter = new HistoryAdapter(HistoryFragment.this, emiArrayList);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(historyAdapter);
            } else {
                emiArrayList = new ArrayList<>();
                recyclerView.setVisibility(View.GONE);
                noAnyHistory.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            emiArrayList = new ArrayList<>();
            recyclerView.setVisibility(View.GONE);
            noAnyHistory.setVisibility(View.VISIBLE);
        }
    }


    private void init(View view) {
        back = view.findViewById(R.id.back);
        delete = view.findViewById(R.id.delete);
        recyclerView = view.findViewById(R.id.history_recyclerview);
        noAnyHistory = view.findViewById(R.id.noAnyHistory);
    }
}