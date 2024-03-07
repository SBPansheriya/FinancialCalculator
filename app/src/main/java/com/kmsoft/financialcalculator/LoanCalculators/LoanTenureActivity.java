package com.kmsoft.financialcalculator.LoanCalculators;

import androidx.appcompat.app.AppCompatActivity;
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

public class LoanTenureActivity extends AppCompatActivity {

    ImageView back;
    LinearLayout allCalculation, percentagePie;
    EditText principalAmount, interest, emi;
    Button calculate, reset;
    PieChart pieChart;
    TextView amountPercentage, totalPaymentPercentage, totalInterestAmount, totalPrincipal, totalPayment, totalLoanTenure;
    static double totalInterest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_tenure);
        getWindow().setStatusBarColor(Color.rgb(13,91,104));

        init();

        back.setOnClickListener(v -> onBackPressed());

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(principalAmount.getText().toString()) && TextUtils.isEmpty(emi.getText().toString()) && TextUtils.isEmpty(interest.getText().toString())) {
                Toast.makeText(LoanTenureActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount.getText().toString())) {
                Toast.makeText(LoanTenureActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(emi.getText().toString())) {
                Toast.makeText(LoanTenureActivity.this, "Please Enter monthly emi amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(interest.getText().toString())) {
                Toast.makeText(LoanTenureActivity.this, "Please Enter interest rate", Toast.LENGTH_SHORT).show();
            } else {
                String principal = principalAmount.getText().toString();
                String emiMonthly = emi.getText().toString();
                String interestRate = interest.getText().toString();

                double finalPrincipalAmount = Double.parseDouble(principal);
                double finalEmiMonthlyAmount = Double.parseDouble(emiMonthly);
                double finalInterestRate = Double.parseDouble(interestRate);

                String loanTenure = calculateTenure(finalPrincipalAmount,finalEmiMonthlyAmount,finalInterestRate);

                DecimalFormat df = new DecimalFormat("#.##");

                double total = totalInterest1 + finalPrincipalAmount;

                if (!Double.isFinite(total)) {
                    Toast.makeText(LoanTenureActivity.this, "Invalid Calculation", Toast.LENGTH_SHORT).show();
                } else {
                    allCalculation.setVisibility(View.VISIBLE);
                    percentagePie.setVisibility(View.VISIBLE);

                    totalInterestAmount.setText(df.format(totalInterest1));
                    totalPrincipal.setText(df.format(finalPrincipalAmount));
                    totalPayment.setText(df.format(total));
                    totalLoanTenure.setText(loanTenure);

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
            interest.setText("");
            principalAmount.setText("");

            allCalculation.setVisibility(View.GONE);
            percentagePie.setVisibility(View.GONE);
        });
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
        Typeface typeface = ResourcesCompat.getFont(LoanTenureActivity.this, R.font.lexenddeca_extrabold);
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
        typeface = ResourcesCompat.getFont(LoanTenureActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }

    private static String calculateTenure(double principalAmount, double EMI, double interestRate) {

        interestRate /= 100;

        double monthlyInterestRate = interestRate / 12;

        double tenureInMonths = -(Math.log(1 - (principalAmount * monthlyInterestRate) / EMI) / Math.log(1 + monthlyInterestRate));

        double tenureInYears = tenureInMonths / 12;

        double totalAmountPaid = EMI * 12 * tenureInYears;
        totalInterest1 = totalAmountPaid - principalAmount;

        int years = (int) Math.floor(tenureInMonths / 12);
        int months = (int) Math.round(tenureInMonths % 12);

        if (months == 12) {
            years++;
            months = 0;
        }

        StringBuilder tenureString = new StringBuilder();
        if (years > 0) {
            tenureString.append(years).append(" year").append(years > 1 ? "s" : "");
            if (months > 0)
                tenureString.append(" and ");
        }
        if (months > 0)
            tenureString.append(months).append(" month").append(months > 1 ? "s" : "");
        return tenureString.toString();
    }

    private void init() {
        back = findViewById(R.id.back);
        allCalculation = findViewById(R.id.all_calculation);
        percentagePie = findViewById(R.id.percentage_pie);
        principalAmount = findViewById(R.id.principal_amount);
        interest = findViewById(R.id.interest);
        emi = findViewById(R.id.emi);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        amountPercentage = findViewById(R.id.amount_percentage);
        totalPaymentPercentage = findViewById(R.id.total_payment_percentage);
        totalInterestAmount = findViewById(R.id.total_interest_amount);
        totalPrincipal = findViewById(R.id.total_principal);
        totalPayment = findViewById(R.id.total_payment);
        totalLoanTenure = findViewById(R.id.total_loan_tenure);
        pieChart = findViewById(R.id.pie_chart);
    }
}