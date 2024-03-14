package com.kmsoft.financialcalculator.BankingCalculation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RDCalculatorActivity extends AppCompatActivity {

    ImageView back,replace,calender;
    EditText monthlyAmount, rateInterest, loanTenure;
    Button calculate, reset;
    LinearLayout dateSelected,linear;
    TextView date, maturityValue, investmentAmount, totalInterest, investmentDate, maturityDate,year,month;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdcalculator);
        getWindow().setStatusBarColor(Color.rgb(13,91,104));

        init();

        back.setOnClickListener(v -> onBackPressed());

        dateSelected.setOnClickListener(v -> showDatePickerDialog());

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(monthlyAmount.getText().toString()) && TextUtils.isEmpty(rateInterest.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(RDCalculatorActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(monthlyAmount.getText().toString())) {
                Toast.makeText(RDCalculatorActivity.this, "Please Enter deposit amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(rateInterest.getText().toString())) {
                Toast.makeText(RDCalculatorActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(RDCalculatorActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(date.getText().toString())) {
                Toast.makeText(RDCalculatorActivity.this, "Please Enter investment Date", Toast.LENGTH_SHORT).show();
            } else {
                String deposit = monthlyAmount.getText().toString();
                String rateOfInterest = rateInterest.getText().toString();
                String loanMonth = loanTenure.getText().toString();
                String investDate = date.getText().toString();

                double finalDepositAmount = Double.parseDouble(deposit);
                double finalRateOfInterest = Double.parseDouble(rateOfInterest);
                int finalLoanMonth = Integer.parseInt((loanMonth));

                if (finalRateOfInterest > 50) {
                    Toast.makeText(RDCalculatorActivity.this, "You can't add 50% above interest Rate", Toast.LENGTH_SHORT).show();
                    return;
                }

                int finalLoanYear = finalLoanMonth / 12;
                int quarter = 4;
                int numberOfQuarters = quarter * finalLoanYear;
                double rate = finalRateOfInterest / 400.0;

                double maturityAmount = calculateMaturityValue(finalDepositAmount,numberOfQuarters,rate);

                linear.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObject;
                try {
                    dateObject = dateFormat.parse(investDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (dateObject != null) {
                    calendar.setTime(dateObject);
                }
                calendar.add(Calendar.YEAR, finalLoanYear);
                Date maturityDate1 = calendar.getTime();
                String finalMaturityDate = dateFormat.format(maturityDate1);

                String[] components = investDate.split("/");

                int day = Integer.parseInt(components[0]);
                int month = Integer.parseInt(components[1]);
                int year = Integer.parseInt(components[2]);

                String formattedDay = (day < 10) ? "0" + day : String.valueOf(day);
                String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);

                String formattedDateString = formattedDay + "/" + formattedMonth + "/" + year;

                double investmentAmountRD = finalDepositAmount * finalLoanMonth;
                double interestAmountRD = maturityAmount - investmentAmountRD;

                DecimalFormat df = new DecimalFormat("#.##");

                maturityValue.setText(df.format(maturityAmount));
                investmentAmount.setText(df.format(investmentAmountRD));
                totalInterest.setText(df.format(interestAmountRD));
                investmentDate.setText(formattedDateString);
                maturityDate.setText(finalMaturityDate);
            }
        });

        reset.setOnClickListener(v -> {
            monthlyAmount.setText("");
            rateInterest.setText("");
            loanTenure.setText("");
            date.setText("");
            selectedDate = null;
            linear.setVisibility(View.GONE);
        });
    }

    private void showDatePickerDialog() {
        int day;
        int month;
        int year;

        if (selectedDate != null) {
            String[] dateParts = selectedDate.split("/");
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]) - 1;
            year = Integer.parseInt(dateParts[2]);
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
            date.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    public static double calculateMaturityValue(double monthlyInstallment, int numberOfQuarters, double rate) {
        double numerator = Math.pow(1 + rate, numberOfQuarters) - 1;
        double denominator = 1 - Math.pow(1 + rate, -(1.0 / 3.0));
        return monthlyInstallment * (numerator / denominator);
    }

    private void init() {
        back = findViewById(R.id.back);
        monthlyAmount = findViewById(R.id.monthly_amount);
        rateInterest = findViewById(R.id.rate_interest);
        loanTenure = findViewById(R.id.loan_tenure);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        date = findViewById(R.id.date);
        maturityValue = findViewById(R.id.maturity_value);
        investmentAmount = findViewById(R.id.investment_amount);
        totalInterest = findViewById(R.id.total_interest);
        investmentDate = findViewById(R.id.investment_date);
        maturityDate = findViewById(R.id.maturity_date);
        year = findViewById(R.id.year);
        replace = findViewById(R.id.replace);
        month = findViewById(R.id.month);
        calender = findViewById(R.id.calender);
        dateSelected = findViewById(R.id.date_selected);
        linear = findViewById(R.id.linear);
    }
}