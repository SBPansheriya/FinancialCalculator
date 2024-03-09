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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyConversionActivity extends AppCompatActivity {

    EditText amount;
    LinearLayout linear;
    Spinner spinner_from, spinner_to;
    Button calculate, reset;
    TextView conversion_rate, date, result;
    SpinnerAdapter adapter;
    String fromCurrency, toCurrency;
    private String[] countryCodeList, countryNameList;
    private int[] countryFlagList;
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

        adapter = new SpinnerAdapter(this, countryNameList, countryFlagList);
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(31);
        spinner_to.setSelection(81);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting currency conversion data");
        progressDialog.setCanceledOnTouchOutside(false);

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(amount.getText().toString())){
                Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
            } else {
                if (isInternetConnected()) {
                    progressDialog.show();
                    getExchangeRateData();
                    linear.setVisibility(View.VISIBLE);
                    getUpdatedDate();
                } else {
                    Toast.makeText(CurrencyConversionActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset.setOnClickListener(v -> {
            amount.setText("");
            spinner_from.setSelection(31);
            spinner_to.setSelection(81);
            linear.setVisibility(View.GONE);
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

    private void InitialiseStringArrays() {

        countryCodeList = new String[]{
                "AUD", "AUD", "AUD", "AUD", "AUD", "AUD",
                "BGN",
                "BRL",
                "CAD",
                "CHF",
                "CNY",
                "CZK",
                "DKK", "DKK", "DKK",
                "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR",
                "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR", "EUR",
                "GBP", "GBP", "GBP",
                "HKD",
                "HUF",
                "IDR",
                "ILS",
                "INR", "INR",
                "ISK",
                "JPY",
                "KRW",
                "MXN",
                "MYR",
                "NOK",
                "NZD",
                "PHP",
                "PLN",
                "RON",
                "SEK",
                "SGD",
                "THB",
                "TRY",
                "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD", "USD",
                "ZAR", "ZAR", "ZAR"};

        countryFlagList = new int[]{
                R.drawable.australia, R.drawable.nauru, R.drawable.kiribati, R.drawable.cocos, R.drawable.christmas, R.drawable.norfalk,
                R.drawable.bulgaria,
                R.drawable.brazil,
                R.drawable.canada,
                R.drawable.switzerland,
                R.drawable.china,
                R.drawable.czech,
                R.drawable.denmark, R.drawable.faroe, R.drawable.greenland,
                R.drawable.germany, R.drawable.italy, R.drawable.france, R.drawable.spain, R.drawable.portugal,
                R.drawable.ireland, R.drawable.greece, R.drawable.netherlands, R.drawable.belgium, R.drawable.austria,
                R.drawable.finland, R.drawable.slovakia, R.drawable.lithuania, R.drawable.slovenia, R.drawable.monaco,
                R.drawable.malta, R.drawable.cyprus, R.drawable.luxembourg, R.drawable.montenegro, R.drawable.estonia,
                R.drawable.latvia, R.drawable.andorra, R.drawable.reunion, R.drawable.san_marino, R.drawable.guadeloupe,
                R.drawable.martinique, R.drawable.mayotte, R.drawable.aland, R.drawable.saint_pierre, R.drawable.saint_martin,
                R.drawable.united_kingdom, R.drawable.england, R.drawable.scotland,
                R.drawable.hongkong,
                R.drawable.hungary,
                R.drawable.indonesia,
                R.drawable.israel,
                R.drawable.india, R.drawable.bhutan,
                R.drawable.iceland,
                R.drawable.japan,
                R.drawable.korea,
                R.drawable.mexico,
                R.drawable.malaysia,
                R.drawable.norway,
                R.drawable.new_zealand,
                R.drawable.philippine,
                R.drawable.poland,
                R.drawable.romania,
                R.drawable.sweden,
                R.drawable.singapore,
                R.drawable.thailand,
                R.drawable.turkey,
                R.drawable.us, R.drawable.ecuador, R.drawable.zimbabwe, R.drawable.panama, R.drawable.purerto, R.drawable.el_salvador, R.drawable.somalia, R.drawable.guam, R.drawable.palau, R.drawable.marshall, R.drawable.micronesia, R.drawable.marina_isaland,
                R.drawable.south_africa, R.drawable.eswatini, R.drawable.namibia};

        countryNameList = new String[]{"Australia", "Nauru", "Kiribati", "Cocos Islands", "Christmas Island", "Norfolk Island", "Bulgaria", "Brazil", "Canada", "Switzerland", "China", "Czech", "Denmark", "Faroe Islands", "Green land", "Germany", "Italy", "France", "Spain", "Portugal", "Ireland", "Greece", "Netherlands", "Belgium", "Austria", "Finland", "Slovakia", "Lithuania", "Slovenia", "Monaco", "Malta", "Cyprus", "Luxembourg", "Montenegro", "Estonia", "Latvia", "Andorra", "Reunion", "San Marino", "Guadeloupe", "Martinique", "Mayotte", "Aland Islands", "Saint Pierre and Miquelon", "Saint Martin",
                "United Kingdom", "England", "Scotland",
                "Hong Kong",
                "Hungary",
                "Indonesia",
                "Israel",
                "India", "Bhutan",
                "Iceland",
                "Japan",
                "Korea",
                "Mexico",
                "Malaysia",
                "Norway",
                "New Zealand", "Philippine", "Poland", "Romania", "Sweden",
                "Singapore",
                "Thailand",
                "Turkey",
                "United States", "Ecuador", "Zimbabwe", "Panama", "Puerto Rico", "El Salvador", "Somalia", "Guam", "Palau", "Marshall", "Micronesia", "Marina Islands", "South Africa", "Eswatini", "Namibia"};

        Country[] countries = new Country[countryCodeList.length];
        for (int i = 0; i < countryCodeList.length; i++) {
            countries[i] = new Country(countryCodeList[i], countryFlagList[i], countryNameList[i]);
        }

        Arrays.sort(countries);

        for (int i = 0; i < countries.length; i++) {
            countryCodeList[i] = countries[i].code;
            countryFlagList[i] = countries[i].flag;
            countryNameList[i] = countries[i].name;
        }
    }

    public void getExchangeRateData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
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
            handler.post(() -> {
                if (exchangeRate != 0 && !amount.getText().toString().equals("")) {
                    double input = Double.parseDouble(amount.getText().toString());
                    BigDecimal result1 = new BigDecimal(input * exchangeRate);
                    String currencyConversionRate = "1 " + fromCurrency + " = " + (1 * exchangeRate) + " " + toCurrency;
                    conversion_rate.setText(currencyConversionRate);

                    result.setText(String.format("%s", result1.setScale(3, RoundingMode.UP).toPlainString()));
                } else {
                    Toast.makeText(CurrencyConversionActivity.this, "GET FAILED", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
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
                if (dateString != null) {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    try {
                        Date date1 = inputFormat.parse(dateString);
                        String outputDate = null;
                        if (date1 != null) {
                            outputDate = outputFormat.format(date1);
                        }
                        date.setText(outputDate);
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

    static class Country implements Comparable<Country> {
        String code;
        int flag;
        String name;

        public Country(String code, int flag, String name) {
            this.code = code;
            this.flag = flag;
            this.name = name;
        }

        @Override
        public int compareTo(Country other) {
            return this.name.compareTo(other.name);
        }
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
        linear = findViewById(R.id.linear);
    }
}