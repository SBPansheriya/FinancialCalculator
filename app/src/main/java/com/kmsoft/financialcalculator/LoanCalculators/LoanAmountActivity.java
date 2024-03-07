package com.kmsoft.financialcalculator.LoanCalculators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LoanAmountActivity extends AppCompatActivity {

    ImageView back,replace;
    LinearLayout allCalculation, percentagePie;
    EditText emi, interest, loanTenure;
    Button calculate, reset;
    PieChart pieChart;
    TextView year,month,amountPercentage, totalPaymentPercentage, totalInterestAmount, totalPrincipal, totalPayment,emiMonthlyPayment;
    String tag;
    private boolean isYearSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_amount);
        getWindow().setStatusBarColor(Color.rgb(13,91,104));

        init();

        back.setOnClickListener(v -> onBackPressed());

        tag = "Year";

        year.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.history_color));

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(LoanAmountActivity.this, R.color.history_color));
            }
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(emi.getText().toString()) && TextUtils.isEmpty(interest.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(LoanAmountActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emi.getText().toString())) {
                Toast.makeText(LoanAmountActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(interest.getText().toString())) {
                Toast.makeText(LoanAmountActivity.this, "Please Enter interest rate", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(LoanAmountActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else {
                String emiMonthly = emi.getText().toString();
                String interestRate = interest.getText().toString();
                String loanYear = loanTenure.getText().toString();
                int finalLoanYear;
                if (tag.equals("Year")) {
                    finalLoanYear = Integer.parseInt(loanYear) * 12;
                } else {
                    finalLoanYear = Integer.parseInt(loanYear);
                }

                double finalEmiMonthlyAmount = Double.parseDouble(emiMonthly);
                double finalInterestRate = Double.parseDouble(interestRate);

                double principalAmount = calculatePrincipal(finalEmiMonthlyAmount, finalInterestRate, finalLoanYear);
                double totalInterest = calculateTotalInterest(finalEmiMonthlyAmount, finalInterestRate, finalLoanYear);

                DecimalFormat df = new DecimalFormat("#.##");
                String formattedPrincipalAmount = df.format(principalAmount);
                String formattedTotalInterest = df.format(totalInterest);
                long total = (long) (totalInterest + principalAmount);

                allCalculation.setVisibility(View.VISIBLE);
                percentagePie.setVisibility(View.VISIBLE);

                totalInterestAmount.setText(formattedTotalInterest);
                totalPrincipal.setText(formattedPrincipalAmount);
                totalPayment.setText(df.format(total));
                emiMonthlyPayment.setText(String.valueOf(finalEmiMonthlyAmount));

                double principalPercentage = (principalAmount / total) * 100;
                double totalInterestPercentage = (totalInterest / total) * 100;
                double finalPrincipalPercentage = Double.parseDouble(df.format(principalPercentage));
                double finalTotalInterestPercentage = Double.parseDouble(df.format(totalInterestPercentage));

                amountPercentage.setText(String.format("Principal Amount  %s%%", finalPrincipalPercentage));
                totalPaymentPercentage.setText(String.format("Total payment  %s%%", finalTotalInterestPercentage));

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry((float) finalPrincipalPercentage, "Principal Amount"));
                entries.add(new PieEntry((float) finalTotalInterestPercentage, "Total payment"));

                pieChart.invalidate();
                setupPieChart(entries);
            }
        });

        reset.setOnClickListener(v -> {
            emi.setText("");
            interest.setText("");
            loanTenure.setText("");

            allCalculation.setVisibility(View.GONE);
            percentagePie.setVisibility(View.GONE);
        });
    }

    private static double calculatePrincipal(double EMI, double interestRate, double tenureInMonths) {
        interestRate /= 100;
        double monthlyInterestRate = interestRate / 12;
        return EMI * (1 - Math.pow(1 + monthlyInterestRate, -tenureInMonths)) / monthlyInterestRate;
    }

    private static double calculateTotalInterest(double EMI, double interestRate, double tenureInMonths) {
        double totalPayment = EMI * tenureInMonths;
        double principalAmount = calculatePrincipal(EMI, interestRate, tenureInMonths);
        return totalPayment - principalAmount;
    }

    private void setupPieChart(ArrayList<PieEntry> entries) {

        PieDataSet pieDataSet = new PieDataSet(entries, "");

        int[] MY_COLORS = {
                Color.rgb(31, 91, 104),
                Color.rgb(242, 167, 59)};
        pieDataSet.setColors(MY_COLORS);

        Description description = pieChart.getDescription();
        description.setText("");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(10);
        Typeface typeface = ResourcesCompat.getFont(LoanAmountActivity.this, R.font.lexenddeca_extrabold);
        pieData.setValueTypeface(typeface);
        pieData.setValueTextColor(Color.WHITE);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        typeface = ResourcesCompat.getFont(LoanAmountActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private void init() {
        back = findViewById(R.id.back);
        allCalculation = findViewById(R.id.all_calculation);
        percentagePie = findViewById(R.id.percentage_pie);
        emi = findViewById(R.id.emi);
        interest = findViewById(R.id.interest);
        loanTenure = findViewById(R.id.loan_tenure);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        amountPercentage = findViewById(R.id.amount_percentage);
        totalPaymentPercentage = findViewById(R.id.total_payment_percentage);
        totalInterestAmount = findViewById(R.id.total_interest_amount);
        totalPrincipal = findViewById(R.id.total_principal);
        totalPayment = findViewById(R.id.total_payment);
        emiMonthlyPayment = findViewById(R.id.emi_monthly);
        pieChart = findViewById(R.id.pie_chart);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        replace = findViewById(R.id.replace);
    }

}