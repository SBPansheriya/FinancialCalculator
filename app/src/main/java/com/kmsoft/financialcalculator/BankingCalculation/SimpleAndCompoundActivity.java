package com.kmsoft.financialcalculator.BankingCalculation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kmsoft.financialcalculator.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SimpleAndCompoundActivity extends AppCompatActivity {

    TextView year, date, simple, compound, startDate, endDate, siCiInterest, siCiInterestValue, principalAmount, investmentAmount;
    EditText timeInYear, ratePerYear, principal;
    Button calculate, reset;
    LinearLayout startDateSelected, endDateSelected, linear, spinnerLinear;
    ImageView line, line1, back;
    Spinner spinner;
    String tagFind;
    String siCiFind;
    String selectedDate;
    String startEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_and_compound);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        setData();

        ArrayAdapter<String> spinnerAAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item,
                this.getResources().getStringArray(R.array.Select_quarter));
        spinnerAAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        spinner.setAdapter(spinnerAAdapter);

        back.setOnClickListener(v -> onBackPressed());

        year.setOnClickListener(v -> {
            tagFind = "Year";

            year.setBackgroundResource(R.drawable.selected_category);
            date.setBackgroundResource(R.drawable.unselected_category);

            timeInYear.setVisibility(View.VISIBLE);
            startDateSelected.setVisibility(View.GONE);
            endDateSelected.setVisibility(View.GONE);

            year.setTextColor(Color.WHITE);
            date.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        date.setOnClickListener(v -> {
            tagFind = "Date";

            date.setBackgroundResource(R.drawable.selected_category);
            year.setBackgroundResource(R.drawable.unselected_category);

            startDateSelected.setVisibility(View.VISIBLE);
            endDateSelected.setVisibility(View.VISIBLE);
            timeInYear.setVisibility(View.GONE);

            date.setTextColor(Color.WHITE);
            year.setTextColor(ContextCompat.getColor(this, R.color.black));
        });

        simple.setOnClickListener(v -> {
            siCiFind = "Simple";

            spinnerLinear.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
            line1.setVisibility(View.GONE);
            simple.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.dark_blue));
            compound.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.si_ci_text_color));
        });

        compound.setOnClickListener(v -> {
            siCiFind = "Compound";

            spinnerLinear.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            compound.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.dark_blue));
            simple.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.si_ci_text_color));
        });

        startDateSelected.setOnClickListener(v -> {
            startEndDate = "Start";
            showDatePickerDialog();
        });

        endDateSelected.setOnClickListener(v -> {
            startEndDate = "End";
            showDatePickerDialog();
        });

        calculate.setOnClickListener(v -> {
            if (tagFind.equals("Year")) {
                if (TextUtils.isEmpty(principal.getText().toString()) && TextUtils.isEmpty(ratePerYear.getText().toString()) && TextUtils.isEmpty(timeInYear.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(principal.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter deposit amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ratePerYear.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(timeInYear.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
                } else {
                    String deposit = principal.getText().toString();
                    String rateOfInterest = ratePerYear.getText().toString();
                    String loanYear = timeInYear.getText().toString();

                    double finalDepositAmount = Double.parseDouble(deposit);
                    double finalRateOfInterest = Double.parseDouble(rateOfInterest);
                    int finalLoanYear = Integer.parseInt((loanYear));

                    if (siCiFind.equals("Simple")) {

                        double simpleInterest = SI(finalDepositAmount, finalRateOfInterest, finalLoanYear, 1);

                        linear.setVisibility(View.VISIBLE);

                        siCiInterest.setText(R.string.simple_interest);

                        DecimalFormat df = new DecimalFormat("#.##");

                        double finalInterest = simpleInterest - finalDepositAmount;
                        siCiInterestValue.setText(df.format(finalInterest));
                        principalAmount.setText(df.format(finalDepositAmount));
                        investmentAmount.setText(df.format(simpleInterest));

                    } else {
                        int quarter = 0;

                        if (spinner.getSelectedItem().toString().equals("Yearly")) {
                            quarter = 1;
                        } else if (spinner.getSelectedItem().toString().equals("Quarterly")) {
                            quarter = 4;
                        } else if (spinner.getSelectedItem().toString().equals("Half yearly")) {
                            quarter = 2;
                        } else if (spinner.getSelectedItem().toString().equals("Monthly")) {
                            quarter = 12;
                        }

                        double compoundInterest = CI(finalDepositAmount, finalRateOfInterest, finalLoanYear, quarter);

                        linear.setVisibility(View.VISIBLE);

                        siCiInterest.setText(R.string.compound_interest);

                        DecimalFormat df = new DecimalFormat("#.##");

                        double finalInterest = compoundInterest - finalDepositAmount;
                        siCiInterestValue.setText(df.format(finalInterest));
                        principalAmount.setText(df.format(finalDepositAmount));
                        investmentAmount.setText(df.format(compoundInterest));
                    }
                }
            } else {
                if (TextUtils.isEmpty(principal.getText().toString()) && TextUtils.isEmpty(ratePerYear.getText().toString()) && TextUtils.isEmpty(startDate.getText().toString()) && TextUtils.isEmpty(endDate.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(principal.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter deposit amount", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ratePerYear.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(startDate.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter start date", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(endDate.getText().toString())) {
                    Toast.makeText(SimpleAndCompoundActivity.this, "Please Enter end date", Toast.LENGTH_SHORT).show();
                } else {

                    String deposit = principal.getText().toString();
                    String rateOfInterest = ratePerYear.getText().toString();
                    String startDateString = startDate.getText().toString();
                    String endDateString = endDate.getText().toString();

                    double finalDepositAmount = Double.parseDouble(deposit);
                    double finalRateOfInterest = Double.parseDouble(rateOfInterest);

                    double finalDays = getCountOfDays(startDateString, endDateString);
                    double years = finalDays / 365.0;

                    if (siCiFind.equals("Simple")) {
                        double simpleInterest = SI(finalDepositAmount, finalRateOfInterest, years, 1);

                        linear.setVisibility(View.VISIBLE);

                        siCiInterest.setText(R.string.simple_interest);

                        DecimalFormat df = new DecimalFormat("#.##");

                        double finalInterest = simpleInterest - finalDepositAmount;
                        siCiInterestValue.setText(df.format(finalInterest));
                        principalAmount.setText(df.format(finalDepositAmount));
                        investmentAmount.setText(df.format(simpleInterest));

                    } else {
                        int quarter = 0;

                        if (spinner.getSelectedItem().toString().equals("Yearly")) {
                            quarter = 1;
                        } else if (spinner.getSelectedItem().toString().equals("Quarterly")) {
                            quarter = 4;
                        } else if (spinner.getSelectedItem().toString().equals("Half yearly")) {
                            quarter = 2;
                        } else if (spinner.getSelectedItem().toString().equals("Monthly")) {
                            quarter = 12;
                        }

                        double compoundInterest = CI(finalDepositAmount, finalRateOfInterest, years, quarter);

                        linear.setVisibility(View.VISIBLE);

                        siCiInterest.setText(R.string.compound_interest);

                        DecimalFormat df = new DecimalFormat("#.##");

                        double finalInterest = compoundInterest - finalDepositAmount;
                        siCiInterestValue.setText(df.format(finalInterest));
                        principalAmount.setText(df.format(finalDepositAmount));
                        investmentAmount.setText(df.format(compoundInterest));
                    }
                }
            }
        });

        reset.setOnClickListener(v -> {
            principal.setText("");
            startDate.setText("");
            endDate.setText("");
            timeInYear.setText("");
            ratePerYear.setText("");

            spinner.setSelection(0);

            linear.setVisibility(View.GONE);
        });
    }

    public long getCountOfDays(String startDateString, String endDateString) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        long differenceInDays = 0;
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            long differenceInMilliseconds = 0;
            if (endDate != null) {
                if (startDate != null) {
                    differenceInMilliseconds = endDate.getTime() - startDate.getTime();
                }
            }
            differenceInDays = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS);

            System.out.println("Difference in days: " + differenceInDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return differenceInDays;
    }

    //Simple Interest
    public static double SI(double principal, double interestRate, double time, int quarters) {
        double time1 = time / quarters;
        double rate = interestRate / 100;
        return principal * (1 + (rate * time1));
    }

    // Compound Interest
    public static double CI(double principal, double annualInterestRate, double numberOfTimesCompoundedPerYear, int quarters) {
        double ratePerPeriod = annualInterestRate / (quarters * 100);
        double totalNumberOfPeriods = numberOfTimesCompoundedPerYear * quarters;
        return principal * Math.pow(1 + ratePerPeriod, totalNumberOfPeriods);
    }

    private void setData() {
        tagFind = "Year";
        year.setBackgroundResource(R.drawable.selected_category);
        date.setBackgroundResource(R.drawable.unselected_category);
        timeInYear.setVisibility(View.VISIBLE);
        startDateSelected.setVisibility(View.GONE);
        endDateSelected.setVisibility(View.GONE);
        year.setTextColor(Color.WHITE);
        date.setTextColor(ContextCompat.getColor(this, R.color.black));

        line.setVisibility(View.VISIBLE);
        line1.setVisibility(View.GONE);
        simple.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.dark_blue));
        compound.setTextColor(ContextCompat.getColor(SimpleAndCompoundActivity.this, R.color.si_ci_text_color));
        siCiFind = "Simple";
    }

    private void showDatePickerDialog() {
        int day;
        int month;
        int year;
        if (TextUtils.equals(startEndDate, "Start")) {
            if (!TextUtils.isEmpty(startDate.getText())) {
                String[] dateParts = startDate.getText().toString().split("/");
                day = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]) - 1;
                year = Integer.parseInt(dateParts[2]);
            } else {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            if (!TextUtils.isEmpty(endDate.getText())) {
                String[] dateParts = endDate.getText().toString().split("/");
                day = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]) - 1;
                year = Integer.parseInt(dateParts[2]);
            } else {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (monthOfYear) + "/" + year1;

            if (TextUtils.equals(startEndDate, "Start")) {
                boolean b = checkDates1(selectedDate, endDate.getText().toString());
                if (!b) {
                    startDate.setText(selectedDate);
                } else {
                    startDate.setText("");
                    endDate.setText("");
                    Toast.makeText(this, "Please enter valid dates", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean b = checkDates(selectedDate, startDate.getText().toString());
                if (!b) {
                    endDate.setText(selectedDate);
                } else {
                    startDate.setText("");
                    endDate.setText("");
                    Toast.makeText(this, "Please enter valid dates", Toast.LENGTH_SHORT).show();
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public static boolean checkDates(String selectedDate, String anotherDate) {
        boolean b = false;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        try {
            if (dfDate.parse(selectedDate).before(dfDate.parse(anotherDate))) {
                b = true;
            } else if (dfDate.parse(selectedDate).equals(dfDate.parse(anotherDate))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean checkDates1(String selectedDate, String anotherDate) {
        boolean b = false;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        try {
            if (dfDate.parse(selectedDate).after(dfDate.parse(anotherDate))) {
                b = true;
            } else if (dfDate.parse(selectedDate).equals(dfDate.parse(anotherDate))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    private void init() {
        back = findViewById(R.id.back);
        year = findViewById(R.id.year);
        date = findViewById(R.id.date);
        simple = findViewById(R.id.simple);
        compound = findViewById(R.id.compound);
        line = findViewById(R.id.line);
        line1 = findViewById(R.id.line1);
        spinner = findViewById(R.id.spinner);
        timeInYear = findViewById(R.id.time_in_year);
        startDateSelected = findViewById(R.id.start_date_selected);
        endDateSelected = findViewById(R.id.end_date_selected);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        principal = findViewById(R.id.principal);
        ratePerYear = findViewById(R.id.rate_per_year);
        siCiInterest = findViewById(R.id.si_ci_interest);
        siCiInterestValue = findViewById(R.id.si_ci_interest_value);
        investmentAmount = findViewById(R.id.investment_amount);
        linear = findViewById(R.id.linear);
        spinnerLinear = findViewById(R.id.spinner_linear);
        principalAmount = findViewById(R.id.principal_amount);
    }
}