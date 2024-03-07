package com.kmsoft.financialcalculator.EMICalculators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class CompareLoanActivity extends AppCompatActivity {

    ImageView back,replace;
    EditText principalAmount1, interest1, loanTenure1,principalAmount2, interest2, loanTenure2;
    Button calculate, reset;
    LinearLayout allCalculation;
    TextView year, month, totalInterest1, totalPayment1, emi1, totalInterest2, totalPayment2, emi2, differenceEmi, differencePrincipal, differencePayment;
    private boolean isYearSelected = true;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_loan);
        getWindow().setStatusBarColor(Color.rgb(13,91,104));

        init();

        back.setOnClickListener(v -> onBackPressed());

        tag = "Year";
        year.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.history_color));

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(CompareLoanActivity.this, R.color.history_color));
            }
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(principalAmount1.getText().toString()) && TextUtils.isEmpty(interest1.getText().toString()) && TextUtils.isEmpty(loanTenure1.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount1.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(interest1.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter interest rate", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure1.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount2.getText().toString()) && TextUtils.isEmpty(interest2.getText().toString()) && TextUtils.isEmpty(loanTenure2.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(principalAmount2.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter principal amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(interest2.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter interest rate", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure2.getText().toString())) {
                Toast.makeText(CompareLoanActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else {
                String principal1 = principalAmount1.getText().toString();
                String interestRate1 = interest1.getText().toString();
                String loanYear1 = loanTenure1.getText().toString();

                String principal2 = principalAmount2.getText().toString();
                String interestRate2 = interest2.getText().toString();
                String loanYear2 = loanTenure2.getText().toString();

                int finalLoanYear1;
                int finalLoanYear2;
                if (tag.equals("Year")) {
                    finalLoanYear1 = Integer.parseInt(loanYear1) * 12;
                    finalLoanYear2 = Integer.parseInt(loanYear2) * 12;
                } else {
                    finalLoanYear1 = Integer.parseInt(loanYear1);
                    finalLoanYear2 = Integer.parseInt(loanYear2);
                }

                double finalPrincipalAmount1 = Double.parseDouble(principal1);
                double finalInterestRate1 = Double.parseDouble(interestRate1);

                double finalPrincipalAmount2 = Double.parseDouble(principal2);
                double finalInterestRate2 = Double.parseDouble(interestRate2);

                double EMI1 = calculateEMI(finalPrincipalAmount1, finalInterestRate1, finalLoanYear1);
                double totalInterest1 = calculateTotalInterest(finalPrincipalAmount1, finalInterestRate1, finalLoanYear1);

                double EMI2 = calculateEMI(finalPrincipalAmount2, finalInterestRate2, finalLoanYear2);
                double totalInterest2 = calculateTotalInterest(finalPrincipalAmount2, finalInterestRate2, finalLoanYear2);

                DecimalFormat df = new DecimalFormat("#.##");
                String formattedEMI1 = df.format(EMI1);
                String formattedEMI2 = df.format(EMI2);

                double total1 = totalInterest1 + finalPrincipalAmount1;
                double total2 = totalInterest2 + finalPrincipalAmount2;

                allCalculation.setVisibility(View.VISIBLE);

                emi1.setText(String.format("%s", formattedEMI1));
                this.totalInterest1.setText(df.format(totalInterest2));
                totalPayment1.setText(df.format(total1));

                emi2.setText(String.format("%s", formattedEMI2));
                this.totalInterest2.setText(df.format(totalInterest2));
                totalPayment2.setText(df.format(total2));

                double diEMI = EMI1 - EMI2;
                double diInterest = totalInterest1 - totalInterest2;
                double diPayment = total1 - total2;

                differenceEmi.setText(df.format(diEMI));
                differencePayment.setText(df.format(diInterest));
                differencePrincipal.setText(df.format(diPayment));
            }
        });

        reset.setOnClickListener(v -> {
            principalAmount1.setText("");
            principalAmount2.setText("");
            interest1.setText("");
            interest2.setText("");
            loanTenure1.setText("");
            loanTenure2.setText("");

            allCalculation.setVisibility(View.GONE);
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

    private void init() {
        back = findViewById(R.id.back);
        principalAmount1 = findViewById(R.id.principal_amount1);
        interest1 = findViewById(R.id.interest1);
        loanTenure1 = findViewById(R.id.loan_tenure1);
        principalAmount2 = findViewById(R.id.principal_amount2);
        interest2 = findViewById(R.id.interest2);
        loanTenure2 = findViewById(R.id.loan_tenure2);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        totalInterest1 = findViewById(R.id.total_principal1);
        totalPayment1 = findViewById(R.id.total_payment1);
        emi1 = findViewById(R.id.total_emi1);
        totalInterest2 = findViewById(R.id.total_principal2);
        totalPayment2 = findViewById(R.id.total_payment2);
        emi2 = findViewById(R.id.total_emi2);
        differenceEmi = findViewById(R.id.difference_emi);
        differencePrincipal = findViewById(R.id.difference_principal);
        differencePayment = findViewById(R.id.difference_payment);
        allCalculation = findViewById(R.id.all_calculation);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        replace = findViewById(R.id.replace);
    }
}