package com.kmsoft.financialcalculator.GSTAndVAT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;

public class VATCalculatorActivity extends AppCompatActivity {

    ImageView back;
    EditText initialAmount, customVatRate;
    CheckBox percentage1, percentage4, percentage5, percentage12, other;
    LinearLayout linear;
    Button addVat, removeVat, calculate, reset;
    TextView original_cost, vatPrice, netPrice;
    String tag;
    double finalRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vatcalculator);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        percentage5.setChecked(true);
        finalRate = 5.0;
        tag = "AddVAT";

        addVat.setBackgroundResource(R.drawable.selected);
        removeVat.setBackgroundResource(R.drawable.unselected);

        addVat.setTextColor(Color.WHITE);
        removeVat.setTextColor(ContextCompat.getColor(VATCalculatorActivity.this, R.color.dark_blue));

        back.setOnClickListener(v -> onBackPressed());

        percentage1.setOnClickListener(v -> {
            customVatRate.setVisibility(View.GONE);
            finalRate = 1.0;
            percentage1.setChecked(true);
            percentage4.setChecked(false);
            percentage5.setChecked(false);
            percentage12.setChecked(false);
            other.setChecked(false);
        });

        percentage4.setOnClickListener(v -> {
            customVatRate.setVisibility(View.GONE);
            finalRate = 4.0;
            percentage1.setChecked(false);
            percentage4.setChecked(true);
            percentage5.setChecked(false);
            percentage12.setChecked(false);
            other.setChecked(false);
        });

        percentage5.setOnClickListener(v -> {
            customVatRate.setVisibility(View.GONE);
            finalRate = 5.0;
            percentage1.setChecked(false);
            percentage4.setChecked(false);
            percentage5.setChecked(true);
            percentage12.setChecked(false);
            other.setChecked(false);
        });

        percentage12.setOnClickListener(v -> {
            customVatRate.setVisibility(View.GONE);
            finalRate = 12.5;
            percentage1.setChecked(false);
            percentage4.setChecked(false);
            percentage5.setChecked(false);
            percentage12.setChecked(true);
            other.setChecked(false);
        });

        other.setOnClickListener(v -> {
            customVatRate.setVisibility(View.VISIBLE);
            percentage1.setChecked(false);
            percentage4.setChecked(false);
            percentage5.setChecked(false);
            percentage12.setChecked(false);
            other.setChecked(true);
        });

        addVat.setOnClickListener(v -> {

            tag = "AddVAT";

            addVat.setBackgroundResource(R.drawable.selected);
            removeVat.setBackgroundResource(R.drawable.unselected);

            addVat.setTextColor(Color.WHITE);
            removeVat.setTextColor(ContextCompat.getColor(VATCalculatorActivity.this, R.color.dark_blue));

        });

        removeVat.setOnClickListener(v -> {

            tag = "RemoveVAT";

            removeVat.setBackgroundResource(R.drawable.selected);
            addVat.setBackgroundResource(R.drawable.unselected);

            removeVat.setTextColor(Color.WHITE);
            addVat.setTextColor(ContextCompat.getColor(VATCalculatorActivity.this, R.color.dark_blue));

        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(initialAmount.getText().toString()) && TextUtils.isEmpty(customVatRate.getText().toString())) {
                Toast.makeText(VATCalculatorActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(initialAmount.getText().toString())) {
                Toast.makeText(VATCalculatorActivity.this, "Please Enter initial amount", Toast.LENGTH_SHORT).show();
            } else if (other.isChecked() && TextUtils.isEmpty(customVatRate.getText().toString())) {
                Toast.makeText(VATCalculatorActivity.this, "Please Enter valid rate", Toast.LENGTH_SHORT).show();
            } else {
                String initial = initialAmount.getText().toString();
                String rate = customVatRate.getText().toString();

                double finalInitialAmount = Double.parseDouble(initial);
                double finalRateInterest;
                if (other.isChecked()) {
                    finalRateInterest = Double.parseDouble(rate);
                } else {
                    finalRateInterest = finalRate;
                }

                if (tag.equals("AddVAT")) {
                    double vatAmountExclusive = calculateVATExclusive(finalInitialAmount, finalRateInterest);
                    double vatNetPriceExclusive = calculateNetPriceVATExclusive(finalInitialAmount, vatAmountExclusive);

                    linear.setVisibility(View.VISIBLE);
                    DecimalFormat df = new DecimalFormat("#.##");

                    original_cost.setText(df.format(finalInitialAmount));
                    vatPrice.setText(df.format(vatAmountExclusive));
                    netPrice.setText(df.format(vatNetPriceExclusive));
                } else {
                    double vatAmountInclusive = calculateVATInclusive(finalInitialAmount, finalRateInterest);
                    double vatNetPriceInclusive = calculateNetPriceVATInclusive(finalInitialAmount, vatAmountInclusive);

                    linear.setVisibility(View.VISIBLE);
                    DecimalFormat df = new DecimalFormat("#.##");

                    original_cost.setText(df.format(finalInitialAmount));
                    vatPrice.setText(df.format(vatAmountInclusive));
                    netPrice.setText(df.format(vatNetPriceInclusive));
                }
            }
        });

        reset.setOnClickListener(v -> {
            initialAmount.setText("");
            percentage5.setChecked(true);
            percentage4.setChecked(false);
            percentage1.setChecked(false);
            percentage12.setChecked(false);
            other.setChecked(false);
            customVatRate.setText("");
            customVatRate.setVisibility(View.GONE);

            linear.setVisibility(View.GONE);

            tag = "AddVAT";

            finalRate = 5.0;
            addVat.setBackgroundResource(R.drawable.selected);
            removeVat.setBackgroundResource(R.drawable.unselected);

            addVat.setTextColor(Color.WHITE);
            removeVat.setTextColor(ContextCompat.getColor(VATCalculatorActivity.this, R.color.dark_blue));
        });
    }

    public static double calculateVATExclusive(double price, double vatPercentage) {
        return (price * vatPercentage) / 100;
    }

    public static double calculateNetPriceVATExclusive(double price, double vatAmount) {
        return price + vatAmount;
    }

    // VAT Inclusive
    public static double calculateVATInclusive(double originalCost, double vatPercentage) {
        return originalCost - (originalCost * (100 / (100 + vatPercentage)));
    }

    public static double calculateNetPriceVATInclusive(double originalCost, double vatAmount) {
        return originalCost - vatAmount;
    }

    private void init() {
        back = findViewById(R.id.back);
        initialAmount = findViewById(R.id.initial_amount);
        customVatRate = findViewById(R.id.custom_vat_rate);
        original_cost = findViewById(R.id.original_cost);
        addVat = findViewById(R.id.add_vat);
        removeVat = findViewById(R.id.remove_vat);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        vatPrice = findViewById(R.id.vat_price);
        netPrice = findViewById(R.id.net_price);
        linear = findViewById(R.id.linear);
        percentage1 = findViewById(R.id.percentage1);
        percentage4 = findViewById(R.id.percentage4);
        percentage5 = findViewById(R.id.percentage5);
        percentage12 = findViewById(R.id.percentage12);
        other = findViewById(R.id.other);
    }
}