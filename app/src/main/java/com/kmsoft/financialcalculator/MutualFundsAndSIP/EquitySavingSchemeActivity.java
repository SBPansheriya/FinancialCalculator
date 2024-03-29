package com.kmsoft.financialcalculator.MutualFundsAndSIP;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.kmsoft.financialcalculator.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class EquitySavingSchemeActivity extends AppCompatActivity {

    ImageView line, line1, back, replace, calender;
    EditText depositAmount, rateInterest, loanTenure;
    Button calculate, reset, shareResult, convertPdf;
    TextView sip, lumpSum, heading;
    LinearLayout dateSelected, linear, title6;
    TextView date, totalInvestmentAmount, totalInterestValue, maturityDate, maturityValue, year, month;
    String selectedDate;
    String tag;
    String finaTag;
    private boolean isYearSelected = true;
    double finalDepositAmount;
    double interestAmount;
    double interestAmount1;
    double maturityAmountELSSYearly;
    double maturityAmountELSS;
    double finalInvestment;
    String finalMaturityDate;
    String investDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity_saving_scheme);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        setData();

        back.setOnClickListener(v -> onBackPressed());

        sip.setOnClickListener(v -> {
            finaTag = "Sip";

            heading.setText(R.string.monthly_investment_amount);
            line.setVisibility(View.VISIBLE);
            line1.setVisibility(View.GONE);
            sip.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            lumpSum.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.si_ci_text_color));
        });

        lumpSum.setOnClickListener(v -> {
            finaTag = "LumpSum";

            heading.setText(R.string.investment_amount1);
            line1.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            lumpSum.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            sip.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.si_ci_text_color));
        });

        dateSelected.setOnClickListener(v -> showDatePickerDialog());

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));
            }
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(depositAmount.getText().toString()) && TextUtils.isEmpty(rateInterest.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(EquitySavingSchemeActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(depositAmount.getText().toString())) {
                Toast.makeText(EquitySavingSchemeActivity.this, "Please Enter deposit amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(rateInterest.getText().toString())) {
                Toast.makeText(EquitySavingSchemeActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(EquitySavingSchemeActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(date.getText().toString())) {
                Toast.makeText(EquitySavingSchemeActivity.this, "Please Enter investment date", Toast.LENGTH_SHORT).show();
            } else {
                String deposit = depositAmount.getText().toString();
                String rateOfInterest = rateInterest.getText().toString();
                String loanYear = loanTenure.getText().toString();
                investDate = date.getText().toString();

                finalDepositAmount = Double.parseDouble(deposit);
                double finalRateOfInterest = Double.parseDouble(rateOfInterest);
                if (finalRateOfInterest > 50) {
                    Toast.makeText(EquitySavingSchemeActivity.this, "You can't add 50% above interest Rate", Toast.LENGTH_SHORT).show();
                    return;
                }

                double finalLoanYear;
                if (tag.equals("Year")) {
                    finalLoanYear = Double.parseDouble((loanYear));
                } else {
                    finalLoanYear = Double.parseDouble(loanYear) / 12;
                }
                if (tag.equals("Year")) {
                    if (finalLoanYear > 30) {
                        Toast.makeText(EquitySavingSchemeActivity.this, "You can't add 30 year above loan tenure", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    double finalLoanMonth = Double.parseDouble(loanYear);
                    if (finalLoanMonth > 360) {
                        Toast.makeText(EquitySavingSchemeActivity.this, "You can't add 360 month above loan tenure", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //Monthly
                maturityAmountELSS = calculateELSSFutureValueMonthly(finalDepositAmount, finalRateOfInterest, (finalLoanYear * 12));

                //Yearly
                maturityAmountELSSYearly = calculateELSSFutureValueYearly(finalDepositAmount, finalRateOfInterest, finalLoanYear);

                DecimalFormat df = new DecimalFormat("#.##");

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
                calendar.add(Calendar.YEAR, (int) finalLoanYear);
                Date maturityDate1 = calendar.getTime();
                finalMaturityDate = dateFormat.format(maturityDate1);

                linear.setVisibility(View.VISIBLE);
                title6.setVisibility(View.VISIBLE);

                if (finaTag.equals("LumpSum")) {
                    interestAmount = maturityAmountELSSYearly - finalDepositAmount;
                    totalInvestmentAmount.setText(df.format(finalDepositAmount));
                    totalInterestValue.setText(df.format(interestAmount));
                    maturityValue.setText(df.format(maturityAmountELSSYearly));
                    maturityDate.setText(finalMaturityDate);
                } else {
                    finalInvestment = finalDepositAmount * finalLoanYear * 12;
                    interestAmount1 = maturityAmountELSS - finalInvestment;

                    totalInvestmentAmount.setText(df.format(finalInvestment));
                    totalInterestValue.setText(df.format(interestAmount1));
                    maturityValue.setText(df.format(maturityAmountELSS));
                    maturityDate.setText(finalMaturityDate);
                }
                shareResult.setBackgroundResource(R.drawable.border2);
                convertPdf.setBackgroundResource(R.drawable.border2);
                shareResult.setTextColor(Color.WHITE);
                convertPdf.setTextColor(Color.WHITE);
                shareResult.setEnabled(true);
                convertPdf.setEnabled(true);
            }
        });

        reset.setOnClickListener(v -> {
            depositAmount.setText("");
            rateInterest.setText("");
            loanTenure.setText("");
            date.setText("");

            finaTag = "Sip";

            heading.setText(R.string.monthly_investment_amount);
            line.setVisibility(View.VISIBLE);
            line1.setVisibility(View.GONE);
            sip.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            lumpSum.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.si_ci_text_color));

            tag = "Year";
            year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));

            selectedDate = null;

            linear.setVisibility(View.GONE);
            title6.setVisibility(View.GONE);
        });

        shareResult.setOnClickListener(v -> {
            DecimalFormat df = new DecimalFormat("#.##");
            StringBuilder shareText = new StringBuilder();

            if (finaTag.equals("LumpSum")) {
                shareText.append("Total Investment Amount : ").append(df.format(finalDepositAmount)).append("\n");
                shareText.append("Total Interest Amount : ").append(df.format(interestAmount)).append("\n");
                shareText.append("Maturity Value : ").append(df.format(maturityAmountELSSYearly)).append("\n");
                shareText.append("Investment Date : ").append(investDate).append("\n");
                shareText.append("Maturity Date : ").append(finalMaturityDate).append("\n");
            } else {
                shareText.append("Total Investment Amount : ").append(df.format(finalInvestment)).append("\n");
                shareText.append("Total Interest Amount : ").append(df.format(interestAmount1)).append("\n");
                shareText.append("Maturity Value : ").append(df.format(maturityAmountELSS)).append("\n");
                shareText.append("Investment Date : ").append(investDate).append("\n");
                shareText.append("Maturity Date : ").append(finalMaturityDate).append("\n");
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            startActivity(Intent.createChooser(shareIntent, "Equity Saving Scheme Calculation"));
        });

        convertPdf.setOnClickListener(v -> checkPermissions());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            generatePDF();
        } else {
            if (ContextCompat.checkSelfPermission(EquitySavingSchemeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestBackupPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                generatePDF();
            }
        }
    }

    private final ActivityResultLauncher<String> requestBackupPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    generatePDF();
                } else {
                    showPermissionDenyDialog(this);
                }
            });

    private void showPermissionDenyDialog(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {
            Dialog dialog = new Dialog(activity);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.permission_denied_first_dialog);
            dialog.setCancelable(false);
            dialog.show();

            Button cancel = dialog.findViewById(R.id.canceldialog);
            Button ok = dialog.findViewById(R.id.okdialog);
            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                checkPermissions();
                dialog.dismiss();
            });
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE)) {

            Dialog dialog = new Dialog(activity);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.permission_denied_first_dialog);
            dialog.setCancelable(false);
            dialog.show();

            Button cancel = dialog.findViewById(R.id.canceldialog);
            Button ok = dialog.findViewById(R.id.okdialog);
            TextView textView = dialog.findViewById(R.id.filename);

            textView.setText(R.string.you_have_chosen_to_never_ask_the_permission_again_but_write_external_storage_permission_is_needed_for_proper_functioning_of_app);
            ok.setText(R.string.enable_from_settings);

            cancel.setOnClickListener(view -> dialog.dismiss());

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                ActivityCompat.startActivityForResult(this, intent, 100, null);
                dialog.dismiss();
            });
        }
    }

    private String Path() {
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Financial Calculator";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String random = "Equity Saving Scheme" + new Random().nextInt(100000) + ".pdf";
        return folderPath + "/" + random;
    }

    private void generatePDF() {
        String pdfPath = Path();
        Uri fileUri = FileProvider.getUriForFile(this, "com.kmsoft.financialcalculator.fileprovider", new File(pdfPath));
        Toast.makeText(this, "Pdf creating...", Toast.LENGTH_SHORT).show();

        try {
            PdfWriter writer = new PdfWriter(getContentResolver().openOutputStream(fileUri));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Equity Saving Scheme Calculation")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(20));

            // Add table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setTextAlignment(TextAlignment.CENTER);

            // Add headers
            table.addCell("Total PrincipalAmount");
            table.addCell("Total InterestAmount");
            table.addCell("Maturity Value ");
            table.addCell("Investment Date");
            table.addCell("Maturity Date");

            // Add data
            DecimalFormat df = new DecimalFormat("#.##");
            if (finaTag.equals("LumpSum")) {
                table.addCell(df.format(finalDepositAmount));
                table.addCell(df.format(interestAmount));
                table.addCell(df.format(maturityAmountELSSYearly));
                table.addCell(investDate);
                table.addCell(finalMaturityDate);
            } else {
                table.addCell(df.format(finalInvestment));
                table.addCell(df.format(interestAmount1));
                table.addCell(df.format(maturityAmountELSS));
                table.addCell(investDate);
                table.addCell(finalMaturityDate);
            }

            document.add(table);
            Toast.makeText(this, "Pdf create successfully", Toast.LENGTH_SHORT).show();
            document.close();

            openPdfFile(pdfPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openPdfFile(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, "com.kmsoft.financialcalculator.fileprovider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public static double calculateELSSFutureValueMonthly(double principal, double annualInterestRate, double numberOfPeriods) {
        // Monthly
        double r = annualInterestRate / 1200;
        double numerator = Math.pow(1 + r, numberOfPeriods) - 1;
        double expression1 = numerator / r;
        double expression2 = 1 + r;
        return principal * expression1 * expression2;

    }

    public static double calculateELSSFutureValueYearly(double principal, double annualInterestRate, double numberOfPeriods) {
        // Yearly
        double rate = annualInterestRate / 100;
        return principal * Math.pow(1 + rate, numberOfPeriods);
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

    private void setData() {
        shareResult.setEnabled(false);
        convertPdf.setEnabled(false);

        finaTag = "Sip";

        heading.setText(R.string.monthly_investment_amount);
        line.setVisibility(View.VISIBLE);
        line1.setVisibility(View.GONE);
        sip.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
        lumpSum.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.si_ci_text_color));

        tag = "Year";

        year.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(EquitySavingSchemeActivity.this, R.color.history_color));
    }

    private void init() {
        back = findViewById(R.id.back);
        line = findViewById(R.id.line);
        line1 = findViewById(R.id.line1);
        depositAmount = findViewById(R.id.deposit_amount);
        rateInterest = findViewById(R.id.rate_interest);
        loanTenure = findViewById(R.id.loan_tenure);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        shareResult = findViewById(R.id.share_result);
        convertPdf = findViewById(R.id.convert_pdf);
        date = findViewById(R.id.date);
        totalInvestmentAmount = findViewById(R.id.total_investment_amount);
        totalInterestValue = findViewById(R.id.total_interest_value);
        maturityDate = findViewById(R.id.maturity_date);
        maturityValue = findViewById(R.id.maturity_value);
        year = findViewById(R.id.year);
        replace = findViewById(R.id.replace);
        month = findViewById(R.id.month);
        calender = findViewById(R.id.calender);
        dateSelected = findViewById(R.id.date_selected);
        linear = findViewById(R.id.linear);
        sip = findViewById(R.id.sip);
        lumpSum = findViewById(R.id.lumpsum);
        heading = findViewById(R.id.heading);
        title6 = findViewById(R.id.title6);
    }
}