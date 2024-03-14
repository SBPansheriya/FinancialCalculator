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
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyConversionActivity extends AppCompatActivity {

    EditText amount;
    ImageView back;
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

        adapter = new SpinnerAdapter(this, countryNameList, countryFlagList,countryCodeList);
        spinner_from.setAdapter(adapter);
        spinner_to.setAdapter(adapter);

        spinner_from.setSelection(99);
        spinner_to.setSelection(43);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Getting currency conversion data");
        progressDialog.setCanceledOnTouchOutside(false);

        back.setOnClickListener(v -> onBackPressed());

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(amount.getText().toString())) {
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
            spinner_from.setSelection(99);
            spinner_to.setSelection(43);
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
                "AUD", "ALL", "AFN", "ARS", "AWG", "AZN",
                "BSD", "BDT", "BBD", "BYN", "BZD", "BMD", "BOB", "BAM", "BWP", "BGN", "BRL", "BND", "BTN",
                "KHR", "CAD", "KYD", "CLP", "CNY", "COP", "CRC", "HRK", "CUP", "CZK",
                "DKK", "DOP",
                "EGP",
                "FKP", "FJD",
                "GHS", "GIP", "GTQ", "GGP", "GYD",
                "HNL", "HKD", "HUF",
                "ISK", "INR", "IDR", "IRR", "IMP", "ILS",
                "JMD", "JPY", "JEP",
                "KZT", "KRW", "KGS",
                "LAK", "LBP", "LRD",
                "MKD", "MYR", "MUR", "MXN", "MNT", "MZN",
                "NAD", "NPR", "ANG", "NZD", "NIO", "NGN", "NOK",
                "OMR",
                "PKR", "PAB", "PYG", "PEN", "PHP", "PLN",
                "PLN",
                "RON", "RUB",
                "SHP", "SAR", "RSD", "SCR", "SGD", "SBD", "SOS", "ZAR", "LKR", "SEK", "CHF", "SRD", "BSD",
                "TWD", "THB", "TTD", "TRY", "TVD",
                "UAH", "AED", "GBP", "USD", "UYU", "UZS",
                "VES", "VND",
                "YER"
        };
        countryFlagList = new int[]{
                R.drawable.australia, R.drawable.albania, R.drawable.afghanistan, R.drawable.argentina, R.drawable.aruba, R.drawable.azerbaijan,
                R.drawable.bahamas, R.drawable.bangladesh, R.drawable.barbados, R.drawable.belarus, R.drawable.belize,
                R.drawable.bermuda, R.drawable.bolivia, R.drawable.bosniaandherzegovina, R.drawable.botswana, R.drawable.bulgaria,
                R.drawable.brazil, R.drawable.brunei, R.drawable.bhutan,
                R.drawable.cambodia, R.drawable.canada, R.drawable.cayman, R.drawable.chile, R.drawable.china,
                R.drawable.colombia, R.drawable.costa_rica, R.drawable.croatia, R.drawable.cuba, R.drawable.czech,
                R.drawable.denmark, R.drawable.dominice,
                R.drawable.egypt,
                R.drawable.falkland, R.drawable.fiji,
                R.drawable.ghana, R.drawable.gibraltar, R.drawable.guatemala, R.drawable.guernsey, R.drawable.guyana,
                R.drawable.honduras, R.drawable.hongkong, R.drawable.hungary,
                R.drawable.iceland, R.drawable.india, R.drawable.indonesia, R.drawable.iran, R.drawable.isleofman, R.drawable.israel,
                R.drawable.jemica, R.drawable.japan, R.drawable.estonia,
                R.drawable.kazakhstan, R.drawable.koreasouth, R.drawable.kyrgyzstan,
                R.drawable.laos, R.drawable.lebanon, R.drawable.liberia,
                R.drawable.macedonia, R.drawable.malaysia, R.drawable.mauritius, R.drawable.mexico, R.drawable.mongolia, R.drawable.mozambique,
                R.drawable.namibia, R.drawable.nepal, R.drawable.netherlands, R.drawable.new_zealand, R.drawable.nicaragua, R.drawable.nigeria, R.drawable.norway,
                R.drawable.oman,
                R.drawable.pakistan, R.drawable.panama, R.drawable.paraguay, R.drawable.peru, R.drawable.philippine, R.drawable.poland,
                R.drawable.qatar,
                R.drawable.romania, R.drawable.russia,
                R.drawable.saint_helena, R.drawable.saudi_arabia, R.drawable.serbia, R.drawable.seychelles, R.drawable.singapore,
                R.drawable.solomon_islands, R.drawable.somalia, R.drawable.south_africa, R.drawable.sri_lanka, R.drawable.sweden,
                R.drawable.switzerland, R.drawable.suriname, R.drawable.syria,
                R.drawable.taiwan, R.drawable.thailand, R.drawable.trinidadandtobago, R.drawable.turkey, R.drawable.tuvalu,
                R.drawable.ukraine, R.drawable.unitedarabemirates, R.drawable.united_kingdom, R.drawable.us, R.drawable.uruguay, R.drawable.uzbekistan,
                R.drawable.venezuela, R.drawable.vietnam,
                R.drawable.yemen,
        };
        countryNameList = new String[]{
                "Australia", "Albania", "Afghanistan", "Argentina", "Aruba", "Azerbaijan",
                "Bahamas", "Bangladesh", "Barbados", "Belarus", "Belize", "Bermuda", "Bolivia",
                "Bosnia and Herzegovina", "Botswana", "Bulgaria", "Brazil", "Brunei", "Bhutan",
                "Cambodia", "Canada", "Cayman", "Chile", "China", "Colombia", "Costa Rica", "Croatia", "Cuba", "Czech Republic",
                "Denmark", "Dominican Republic",
                "Egypt",
                "Falkland Islands", "Fiji",
                "Ghana", "Gibraltar", "Guatemala", "Guernsey", "Guyana",
                "Honduras", "Hong kong", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Isle of man", "Israel",
                "Jamaica", "Japan", "Jersey",
                "Kazakhstan", "Korea(South)", "Kyrgyzstan",
                "Laos", "Lebanon", "Liberia",
                "Macedonia", "Malaysia", "Mauritius", "Mexico", "Mongolia", "Mozambique",
                "Namibia", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Nigeria", "Norway",
                "Oman",
                "Pakistan", "Panama", "Paraguay", "Peru", "Philippines", "Poland",
                "Qatar",
                "Romania", "Russia",
                "Saint Helena", "Saudi Arabia", "Serbia", "Seychelles", "Singapore", "Solomon Islands", "Somalia",
                "South Africa", "Sri Lanka", "Sweden", "Switzerland", "Suriname", "Syria",
                "Taiwan", "Thailand", "Trinidad and Tobago", "Turkey", "Tuvalu",
                "Ukraine", "United Arab Emirates", "U.K.", "U.S.", "Uruguay", "Uzbekistan",
                "Venezuela", "Vietnam",
                "Yemen",
        };

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
                String GET_URL = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;
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

            handler.post(() -> {
                if (exchangeRate != 0 && !amount.getText().toString().equals("")) {
                    double input = Double.parseDouble(amount.getText().toString());
                    BigDecimal result1 = new BigDecimal(input * exchangeRate);
                    String currencyConversionRate = "1 " + fromCurrency + " = " + (1 * exchangeRate) + " " + toCurrency;
                    conversion_rate.setText(currencyConversionRate);

                    result.setText(String.format("%s", result1.setScale(2, RoundingMode.UP).toPlainString()));
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
                String GET_URL = "https://api.exchangerate-api.com/v4/latest";
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
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    Date date1 = new Date();
                    String outputDate = null;
                    if (date1 != null) {
                        outputDate = outputFormat.format(date1);
                    }
                    date.setText(outputDate);
                    progressDialog.dismiss();
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
        back = findViewById(R.id.back);
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