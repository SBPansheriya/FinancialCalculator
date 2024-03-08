package com.kmsoft.financialcalculator.GSTAndVAT;

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

public class GSTCalculatorActivity extends AppCompatActivity {

    ImageView back;
    EditText initialAmount, rateInterest;
    LinearLayout linear;
    Button addGST, subGST, calculate, reset;
    TextView netAmount, gstAmount, cgst, sgst, totalAmount;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gstcalculator);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();
        tag = "AddGST";
        addGST.setBackgroundResource(R.drawable.selected);
        subGST.setBackgroundResource(R.drawable.unselected);

        addGST.setTextColor(Color.WHITE);
        subGST.setTextColor(ContextCompat.getColor(GSTCalculatorActivity.this, R.color.dark_blue));

        back.setOnClickListener(v -> onBackPressed());

        addGST.setOnClickListener(v -> {
            tag = "AddGST";
            addGST.setBackgroundResource(R.drawable.selected);
            subGST.setBackgroundResource(R.drawable.unselected);
            addGST.setTextColor(Color.WHITE);
            subGST.setTextColor(ContextCompat.getColor(GSTCalculatorActivity.this, R.color.dark_blue));
        });

        subGST.setOnClickListener(v -> {
            tag = "SubGST";
            subGST.setBackgroundResource(R.drawable.selected);
            addGST.setBackgroundResource(R.drawable.unselected);
            subGST.setTextColor(Color.WHITE);
            addGST.setTextColor(ContextCompat.getColor(GSTCalculatorActivity.this, R.color.dark_blue));
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(initialAmount.getText().toString()) && TextUtils.isEmpty(rateInterest.getText().toString())) {
                Toast.makeText(GSTCalculatorActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(initialAmount.getText().toString())) {
                Toast.makeText(GSTCalculatorActivity.this, "Please Enter initial amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(rateInterest.getText().toString())) {
                Toast.makeText(GSTCalculatorActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
            } else {
                String initial = initialAmount.getText().toString();
                String rate = rateInterest.getText().toString();

                double finalInitialAmount = Double.parseDouble(initial);
                double finalRateInterest = Double.parseDouble(rate);

                linear.setVisibility(View.VISIBLE);
                DecimalFormat df = new DecimalFormat("#.##");

                if (tag.equals("AddGST")) {
                    double gst = calculateGSTExclusive(finalInitialAmount, finalRateInterest);
                    double netPrice = calculateNetPriceExclusive(finalInitialAmount, gst);

                    double halfAmount = gst / 2;
                    double halfPercentage = finalRateInterest / 2;

                    netAmount.setText(df.format(finalInitialAmount));
                    gstAmount.setText(df.format(gst));
                    cgst.setText(String.format("CGST : %s%% = %s", halfPercentage, df.format(halfAmount)));
                    sgst.setText(String.format("SGST : %s%% = %s", halfPercentage, df.format(halfAmount)));
                    totalAmount.setText(df.format(netPrice));
                } else {
                    double gstAmountInclusive = calculateGSTInclusive(finalInitialAmount, finalRateInterest);
                    double netPriceInclusive = calculateNetPriceInclusive(finalInitialAmount, gstAmountInclusive);

                    double halfAmount = gstAmountInclusive / 2;
                    double halfPercentage = finalRateInterest / 2;

                    netAmount.setText(df.format(finalInitialAmount));
                    gstAmount.setText(df.format(gstAmountInclusive));
                    cgst.setText(String.format("CGST : %s%% = %s", halfPercentage, df.format(halfAmount)));
                    sgst.setText(String.format("SGST : %s%% = %s", halfPercentage, df.format(halfAmount)));
                    totalAmount.setText(df.format(netPriceInclusive));
                }
            }
        });

        reset.setOnClickListener(v -> {
            initialAmount.setText("");
            rateInterest.setText("");

            linear.setVisibility(View.GONE);

            tag = "AddGST";

            addGST.setBackgroundResource(R.drawable.selected);
            subGST.setBackgroundResource(R.drawable.unselected);

            addGST.setTextColor(Color.WHITE);
            subGST.setTextColor(ContextCompat.getColor(GSTCalculatorActivity.this, R.color.dark_blue));
        });
    }

    // GST Exclusive
    public static double calculateGSTExclusive(double price, double gstPercentage) {
        return (price * gstPercentage) / 100;
    }

    public static double calculateNetPriceExclusive(double price, double gstAmount) {
        return price + gstAmount;
    }

    // GST Inclusive
    public static double calculateGSTInclusive(double originalCost, double gstPercentage) {
        return originalCost - (originalCost * (100 / (100 + gstPercentage)));
    }

    public static double calculateNetPriceInclusive(double originalCost, double gstAmount) {
        return originalCost - gstAmount;
    }

    private void init() {
        back = findViewById(R.id.back);
        initialAmount = findViewById(R.id.initial_amount);
        rateInterest = findViewById(R.id.rate_interest);
        addGST = findViewById(R.id.add_gst);
        subGST = findViewById(R.id.sub_gst);
        netAmount = findViewById(R.id.net_amount);
        gstAmount = findViewById(R.id.gst_amount);
        cgst = findViewById(R.id.cgst);
        sgst = findViewById(R.id.sgst);
        totalAmount = findViewById(R.id.total_amount);
        linear = findViewById(R.id.linear);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
    }
}