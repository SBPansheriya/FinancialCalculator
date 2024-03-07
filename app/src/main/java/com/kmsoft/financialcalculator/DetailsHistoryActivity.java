package com.kmsoft.financialcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmsoft.financialcalculator.Adapter.HistoryDetailAdapter;
import com.kmsoft.financialcalculator.Model.DetailesEmi;
import com.kmsoft.financialcalculator.Model.EMI;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailsHistoryActivity extends AppCompatActivity {

    ImageView back;
    TextView amount, interest, periodMonths, monthlyEmi, totalInterest, totalPayments;
    RecyclerView recyclerView;
    HistoryDetailAdapter historyDetailAdapter;
    EMI emi;
    ArrayList<DetailesEmi> detailesEmiArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_history);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        emi = (EMI) getIntent().getSerializableExtra("EMI");

        DecimalFormat df = new DecimalFormat("#.##");
        amount.setText(df.format(emi.getPrincipalAmount()));
        interest.setText(df.format(emi.getInterestRate()));
        periodMonths.setText(df.format(emi.getLoanTenure()));
        monthlyEmi.setText(df.format(emi.getMonthlyEmi()));
        totalInterest.setText(df.format(emi.getTotalInterestAmount()));
        totalPayments.setText(df.format(emi.getTotalPayment()));

        back.setOnClickListener(v -> onBackPressed());


        double principal = emi.getPrincipalAmount();
        double rate = emi.getInterestRate()/1200;

        for (int Month = 1; Month <= emi.getLoanTenure(); Month++) {
            double interest = principal * rate;
            double p = emi.getMonthlyEmi() - interest;

            principal -= p;

            DetailesEmi detailesEmi = new DetailesEmi(Month,p,interest,principal);
            detailesEmiArrayList.add(detailesEmi);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        historyDetailAdapter = new HistoryDetailAdapter(DetailsHistoryActivity.this,detailesEmiArrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(historyDetailAdapter);
    }

    private void init() {
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.detail_recyclerview);
        amount = findViewById(R.id.amount);
        interest = findViewById(R.id.interest);
        periodMonths = findViewById(R.id.period_months);
        monthlyEmi = findViewById(R.id.monthly_emi);
        totalInterest = findViewById(R.id.total_interest);
        totalPayments = findViewById(R.id.total_payments);
    }
}