package com.kmsoft.financialcalculator.EMICalculators;

import static com.kmsoft.financialcalculator.Constant.emiArrayList;

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
import com.kmsoft.financialcalculator.DBHelper;
import com.kmsoft.financialcalculator.Model.EMI;
import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EMICalculatorActivity extends AppCompatActivity {

    ImageView back, replace;
    LinearLayout allCalculation, percentagePie;
    EditText principalAmount, interest, loanTenure;
    Button calculate, reset;
    PieChart pieChart;
    TextView amountPercentage, totalPaymentPercentage, totalInterestAmount, totalPrincipal, totalPayment, emi, year, month;
    private boolean isYearSelected = true;
    String tag;
    EMI emi1;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculator);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        dbHelper = new DBHelper(EMICalculatorActivity.this);

        back.setOnClickListener(v -> onBackPressed());

        tag = "Year";
        year.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.history_color));

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(EMICalculatorActivity.this, R.color.history_color));
            }
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(principalAmount.getText().toString()) && TextUtils.isEmpty(interest.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(EMICalculatorActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount.getText().toString())) {
                Toast.makeText(EMICalculatorActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(interest.getText().toString())) {
                Toast.makeText(EMICalculatorActivity.this, "Please Enter interest rate", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(EMICalculatorActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else {
                String principal = principalAmount.getText().toString();
                String interestRate = interest.getText().toString();
                String loanYear = loanTenure.getText().toString();
                int finalLoanYear;
                if (tag.equals("Year")) {
                    finalLoanYear = Integer.parseInt(loanYear) * 12;
                } else {
                    finalLoanYear = Integer.parseInt(loanYear);
                }

                double finalPrincipalAmount = Double.parseDouble(principal);
                double finalInterestRate = Double.parseDouble(interestRate);

                double EMI = calculateEMI(finalPrincipalAmount, finalInterestRate, finalLoanYear);
                double totalInterest = calculateTotalInterest(finalPrincipalAmount, finalInterestRate, finalLoanYear);

                DecimalFormat df = new DecimalFormat("#.##");
                String formattedEMI = df.format(EMI);
                String formattedTotalInterest = df.format(totalInterest);

                allCalculation.setVisibility(View.VISIBLE);
                percentagePie.setVisibility(View.VISIBLE);

                totalInterestAmount.setText(String.format("%s", formattedTotalInterest));
                totalPrincipal.setText(df.format(finalPrincipalAmount));
                double total = totalInterest + finalPrincipalAmount;
                totalPayment.setText(df.format(total));
                emi.setText(String.format("%s", formattedEMI));

                double principalPercentage = (finalPrincipalAmount / total) * 100;
                double emiPercentage = (totalInterest / total) * 100;
                double finalPrincipalPercentage = Double.parseDouble(df.format(principalPercentage));
                double finalEmiPercentage = Double.parseDouble(df.format(emiPercentage));

                amountPercentage.setText(String.format("Principal Amount  %s%%", finalPrincipalPercentage));
                totalPaymentPercentage.setText(String.format("Total payment  %s%%", finalEmiPercentage));

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry((float) finalPrincipalPercentage, "Principal Amount"));
                entries.add(new PieEntry((float) finalEmiPercentage, "Total payment"));

                long tsLong = System.currentTimeMillis() / 1000;
                String currantDateTimeStamp = Long.toString(tsLong);

                emi1 = new EMI(0, finalPrincipalAmount, finalInterestRate, finalLoanYear, totalInterest, EMI, total, currantDateTimeStamp);
                emiArrayList.add(emi1);
                dbHelper.insertData(emi1);

                pieChart.invalidate();
                setupPieChart(entries);
            }
        });

        reset.setOnClickListener(v -> {
            principalAmount.setText("");
            interest.setText("");
            loanTenure.setText("");
            allCalculation.setVisibility(View.GONE);
            percentagePie.setVisibility(View.GONE);
        });
    }

    private static double calculateEMI(double principalAmount, double annualInterestRate, int totalNumberOfPayments) {
        double monthlyInterestRate = annualInterestRate / 1200.0;
        return (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalNumberOfPayments)) / (Math.pow(1 + monthlyInterestRate, totalNumberOfPayments) - 1);
    }

    private static double calculateTotalInterest(double principalAmount, double annualInterestRate, int totalNumberOfPayments) {
        double emi = calculateEMI(principalAmount, annualInterestRate, totalNumberOfPayments);
        return (emi * totalNumberOfPayments) - principalAmount;
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
        Typeface typeface = ResourcesCompat.getFont(EMICalculatorActivity.this, R.font.lexenddeca_extrabold);
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
        typeface = ResourcesCompat.getFont(EMICalculatorActivity.this, R.font.lexenddeca_extrabold);
        pieChart.setCenterTextTypeface(typeface);
    }


    private void init() {
        back = findViewById(R.id.back);
        allCalculation = findViewById(R.id.all_calculation);
        percentagePie = findViewById(R.id.percentage_pie);
        principalAmount = findViewById(R.id.principal_amount);
        interest = findViewById(R.id.interest);
        loanTenure = findViewById(R.id.loan_tenure);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        amountPercentage = findViewById(R.id.amount_percentage);
        totalPaymentPercentage = findViewById(R.id.total_payment_percentage);
        totalInterestAmount = findViewById(R.id.total_interest_amount);
        totalPrincipal = findViewById(R.id.total_principal);
        totalPayment = findViewById(R.id.total_payment);
        emi = findViewById(R.id.emi_monthly);
        pieChart = findViewById(R.id.pie_chart);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        replace = findViewById(R.id.replace);
    }
}