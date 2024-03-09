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

public class LoanRateActivity extends AppCompatActivity {
    ImageView back,replace;
    LinearLayout allCalculation, percentagePie;
    EditText principalAmount, loanTenure, emi;
    Button calculate, reset;
    PieChart pieChart;
    TextView year,month,amountPercentage, totalPaymentPercentage, totalInterestAmount, totalPrincipal, totalPayment, totalInterest;
    String tag;
    private boolean isYearSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_rate);
        getWindow().setStatusBarColor(Color.rgb(13,91,104));

        init();

        tag = "Year";

        year.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.history_color));

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(LoanRateActivity.this, R.color.history_color));
            }
        });

        back.setOnClickListener(v -> onBackPressed());

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(emi.getText().toString()) && TextUtils.isEmpty(principalAmount.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(LoanRateActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount.getText().toString())) {
                Toast.makeText(LoanRateActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emi.getText().toString())) {
                Toast.makeText(LoanRateActivity.this, "Please Enter emi amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(LoanRateActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else {
                String principal = principalAmount.getText().toString();
                String emiMonthly = emi.getText().toString();
                String loanYear = loanTenure.getText().toString();

                double finalLoanYear;

                if (tag.equals("Year")) {
                    finalLoanYear = Double.parseDouble(loanYear);
                } else {
                    finalLoanYear = Double.parseDouble(loanYear) / 12;
                }

                double finalEmiMonthlyAmount = Double.parseDouble(emiMonthly);
                double finalPrincipalAmount = Double.parseDouble(principal);

                double interestRate  = calculateInterestRate(finalPrincipalAmount,finalEmiMonthlyAmount,finalLoanYear);

                DecimalFormat df = new DecimalFormat("#.##");

                double total = finalEmiMonthlyAmount * finalLoanYear * 12;
                double totalInterest1 = total - finalPrincipalAmount;

                if (!Double.isFinite(total)) {
                    Toast.makeText(LoanRateActivity.this, "Invalid Calculation", Toast.LENGTH_SHORT).show();
                } else {
                    allCalculation.setVisibility(View.VISIBLE);
                    percentagePie.setVisibility(View.VISIBLE);

                    totalInterestAmount.setText(df.format(totalInterest1));
                    totalPrincipal.setText(df.format(finalPrincipalAmount));
                    totalPayment.setText(df.format(total));
                    totalInterest.setText(df.format(interestRate));

                    double principalPercentage = (finalPrincipalAmount / total) * 100;
                    double totalInterestPercentage = (totalInterest1 / total) * 100;
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
            }
        });

        reset.setOnClickListener(v -> {
            emi.setText("");
            loanTenure.setText("");
            principalAmount.setText("");

            allCalculation.setVisibility(View.GONE);
            percentagePie.setVisibility(View.GONE);
        });
    }

    public static double calculateInterestRate(double principalAmount, double EMI, double tenureInYears) {

        double totalAmountPaid = EMI * 12 * tenureInYears;
        double totalInterest = totalAmountPaid - principalAmount;
        return (totalInterest / principalAmount) ;
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
        Typeface typeface = ResourcesCompat.getFont(LoanRateActivity.this, R.font.lexenddeca_extrabold);
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
        typeface = ResourcesCompat.getFont(LoanRateActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private void init() {
        back = findViewById(R.id.back);
        allCalculation = findViewById(R.id.all_calculation);
        percentagePie = findViewById(R.id.percentage_pie);
        principalAmount = findViewById(R.id.principal_amount);
        loanTenure = findViewById(R.id.loan_tenure);
        emi = findViewById(R.id.emi);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        amountPercentage = findViewById(R.id.amount_percentage);
        totalPaymentPercentage = findViewById(R.id.total_payment_percentage);
        totalInterestAmount = findViewById(R.id.total_interest_amount);
        totalPrincipal = findViewById(R.id.total_principal);
        totalPayment = findViewById(R.id.total_payment);
        totalInterest = findViewById(R.id.total_interest);
        pieChart = findViewById(R.id.pie_chart);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        replace = findViewById(R.id.replace);
    }
}