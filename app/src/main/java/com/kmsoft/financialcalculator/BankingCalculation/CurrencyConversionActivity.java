package com.kmsoft.financialcalculator.BankingCalculation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.Adapter.SpinnerAdapter;
import com.kmsoft.financialcalculator.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyConversionActivity extends AppCompatActivity {

    EditText amount;
    Spinner spinner_from, spinner_to;
    Button calculate, reset;
    TextView conversion_rate, date, result;
    SpinnerAdapter adapter;
    String fromCurrency, toCurrency;
    private String[] countryCodeList, countryNameList;
    private int[] countryFlagList;
    ArrayAdapter<String> countryCodeAdapter;
    private ArrayList<String> currencyArrayList;
    int fromCountryPosition = 0, toCountryPosition = 0;
    private double exchangeRate = 0;
    private String dateString = "";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conversion);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        InitialiseStringArrays();
        currencyArrayList = new ArrayList<>();

        loadArrayList();

        countryCodeAdapter = new ArrayAdapter<>(CurrencyConversionActivity.this, android.R.layout.simple_spinner_dropdown_item, currencyArrayList);
        spinner_from.setAdapter(countryCodeAdapter);
        spinner_to.setAdapter(countryCodeAdapter);

        spinner_from.setSelection(0);
        spinner_to.setSelection(1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting currency conversion data");
        progressDialog.setCanceledOnTouchOutside(false);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected()) {
                    progressDialog.show();
                    getExchangeRateData();
                    getUpdatedDate();
                } else {
                    Toast.makeText(CurrencyConversionActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromCountryPosition = position;
                toCountryPosition = spinner_to.getSelectedItemPosition();

                fromCurrency = countryCodeList[position];
                toCurrency = countryCodeList[spinner_to.getSelectedItemPosition()];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromCountryPosition = spinner_from.getSelectedItemPosition();
                toCountryPosition = position;

                toCurrency = countryCodeList[toCountryPosition];
                fromCurrency = countryCodeList[fromCountryPosition];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!amount.getText().toString().isEmpty()) {
                    fromCountryPosition = spinner_from.getSelectedItemPosition();
                    toCountryPosition = spinner_to.getSelectedItemPosition();

                    fromCurrency = countryCodeList[fromCountryPosition];
                    toCurrency = countryCodeList[toCountryPosition];
                } else {
                    result.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void init() {
        amount = findViewById(R.id.conversation_amount);
        spinner_from = findViewById(R.id.spinner_from);
        spinner_to = findViewById(R.id.spinner_to);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        conversion_rate = findViewById(R.id.conversion_rate);
        date = findViewById(R.id.date);
        result = findViewById(R.id.result);
    }

    private void InitialiseStringArrays() {
        countryCodeList = new String[]{
                "AUD", "BGN", "BRL", "CAD",
                "CHF", "CNY", "CZK", "DKK",
                "EUR", "GBP", "HKD", "HUF",
                "IDR", "ILS", "INR", "ISK",
                "JPY", "KRW", "MXN", "MYR",
                "NOK", "NZD", "PHP", "PLN",
                "RON", "SEK", "SGD", "THB",
                "TRY", "USD", "ZAR"
        };

        countryFlagList = new int[]{
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india, R.drawable.india,
                R.drawable.india
        };


//        countryNameList1 = new String[]{
//                "Australian Dollar", "Bulgarian Lev", "Brazilian Real", "Canadian Dollar",
//                "Swiss Franc", "Chinese Yuan", "Czech Koruna", "Danish Krone",
//                "Euro", "British Pound", "Hong Kong Dollar", "Hungarian Forint",
//                "Indonesian Rupiah", "Israeli New Sheqel", "Indian Rupee", "Icelandic Króna",
//                "Japanese Yen", "South Korean Won", "Mexican Peso", "Malaysian Ringgit",
//                "Norwegian Krone", "New Zealand Dollar", "Philippine Peso", "Polish Zloty",
//                "Romanian Leu", "Swedish Krona", "Singapore Dollar", "Thai Baht",
//                "Turkish Lira", "US Dollar", "South African Rand"
//        };

        countryNameList = new String[]{
                "Australia","Nauru","Tuvalu","Kiribati","Cocos Islands","Christmas Island","Norfolk Island",
                "Bulgaria",
                "Brazil",
                "Canada",
                "Switzerland", "Liechtenstein",
                "China",
                "Czech",
                "Denmark","Faroe Islands","Green land",
                "Germany", "Italy","France","Spain","Portugal","Ireland","Greece","Netherlands","Belgium","Austria","Finland",
                "Slovakia","Lithuania","Slovenia","Lithuania","Slovenia","Monaco","Malta","Cyprus","Luxembourg","Montenegro",
                "Vatican city","Kosovo","Estonia","Latvia","Andorra","Reunion","San Marino","Guadeloupe","Martinique","Saint Barthelemy",
                "Mayotte","Aland Islands","Saint Pierre and Miquelon","Saint Martin",
                "United Kingdom","England","Scotland","Jersey","Wales","Guernsey","Isle of man","Ireland",
                "Hong Kong",
                "Hungary",
                "Indonesia",
                "Israel",
                "Indian", "Bhutan",
                "Iceland",
                "Japan",
                "Korea",
                "Mexico",
                "Malaysia",
                "Norway","Quisling regime",
                "New Zealand", "Cook Islands","Niue","Pitcairn Island","Tokelau",
                "Philippine",
                "Poland",
                "Romania",
                "Sweden",
                "Singapore",
                "Thailand",
                "Turkish",
                "United States","Ecuador","Zimbabwe","Panama",
                "South African Rand"
        };

    }

    private void loadArrayList() {
        currencyArrayList.clear();

        for (int i = 0; i < countryCodeList.length; ++i) {
            currencyArrayList.add(countryNameList[i] + " (" + countryCodeList[i] + ")");
        }
    }

    public void getExchangeRateData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String GET_URL = "https://api.frankfurter.app/latest?from=" + fromCurrency + "&to=" + toCurrency;
                    URL url = new URL(GET_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");

                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        JSONObject jsonObject = new JSONObject(response.toString());

                        exchangeRate = jsonObject.getJSONObject("rates").getDouble(toCurrency);
                        progressDialog.dismiss();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                //updates the UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (exchangeRate != 0 && !amount.getText().toString().equals("")) {
                            double input = Double.parseDouble(amount.getText().toString());
                            BigDecimal result1 = new BigDecimal(input * exchangeRate);

                            result.setText("" + result1.setScale(3, RoundingMode.UP).toPlainString());

                            String currencyConversionRate = "1 " + fromCurrency + " = " + (1 * exchangeRate) + " " + toCurrency;
                        } else {
                            Toast.makeText(CurrencyConversionActivity.this, "GET FAILED", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void getUpdatedDate() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            try {
                String GET_URL = "https://api.frankfurter.app/latest";
                URL url = new URL(GET_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { //successful
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonObject = new JSONObject(response.toString());

                    dateString = jsonObject.getString("date");
                    progressDialog.dismiss();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            handler.post(() -> {
                if (dateString != null || !dateString.equals("")) {

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

                    try {
                        Date date = inputFormat.parse(dateString);
                        String outputDate = outputFormat.format(date);
//                                lblLastUpdated.setText("Rates as of " + outputDate);
                        progressDialog.dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(CurrencyConversionActivity.this, "GET FAILED", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        });
    }
}