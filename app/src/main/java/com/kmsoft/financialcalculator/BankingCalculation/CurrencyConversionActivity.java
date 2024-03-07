package com.kmsoft.financialcalculator.BankingCalculation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.Adapter.SpinnerAdapter;
import com.kmsoft.financialcalculator.R;

public class CurrencyConversionActivity extends AppCompatActivity {

    EditText amount;
    Spinner spinner_from,spinner_to;
    Button calculate,reset;
    TextView conversion_rate,date,result;
    SpinnerAdapter adapter;
    String[] name1 = {"Albania","Afghanistan","Argentina","Aruba","Australia","Azerbaijan","Bahamas","Bangladesh","Barbados","Bolivia","Bosnia and Herzegovina","Botswana","Bulgaria","Brazil","Brunei","Cambodia","Canada"};
    String[] name = {"Cambodia","Canada"};
    int[] images = {R.drawable.india,R.drawable.india};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);

        init();

        adapter = new SpinnerAdapter(this,name,images);

        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CurrencyConversionActivity.this, "123", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init(){
        amount = findViewById(R.id.conversation_amount);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        conversion_rate = findViewById(R.id.conversion_rate);
        date = findViewById(R.id.date);
        result = findViewById(R.id.result);
    }
}