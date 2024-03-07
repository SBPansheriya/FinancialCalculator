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

public class SIPCalculatorActivity extends AppCompatActivity {

    ImageView back, replace, calender;
    EditText depositAmount, rateInterest, loanTenure;
    TextView calculate, shareResult, convertPdf;
    LinearLayout dateSelected, linear;
    TextView date, totalInvestmentAmount, totalInterestValue, maturityDate, maturityValue, year, month;
    String selectedDate;
    String tag;
    private boolean isYearSelected = true;
    double investmentAmountSIP;
    double interestAmountSIP;
    double SIPFutureValue;
    String finalMaturityDate;
    String investDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sipcalculator);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        shareResult.setEnabled(false);
        convertPdf.setEnabled(false);

        back.setOnClickListener(v -> onBackPressed());

        dateSelected.setOnClickListener(v -> showDatePickerDialog());

        tag = "Year";

        year.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.dark_blue));
        month.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.history_color));

        year.setOnClickListener(v -> {
            tag = "Year";

            year.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.dark_blue));
            month.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.history_color));
        });

        month.setOnClickListener(v -> {
            tag = "Month";

            month.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.dark_blue));
            year.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.history_color));
        });

        replace.setOnClickListener(v -> {
            isYearSelected = !isYearSelected;

            if (isYearSelected) {
                tag = "Year";

                year.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.dark_blue));
                month.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.history_color));
            } else {
                tag = "Month";

                month.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.dark_blue));
                year.setTextColor(ContextCompat.getColor(SIPCalculatorActivity.this, R.color.history_color));
            }
        });

        calculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(depositAmount.getText().toString()) && TextUtils.isEmpty(rateInterest.getText().toString()) && TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(SIPCalculatorActivity.this, "Please Enter valid values", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(depositAmount.getText().toString())) {
                Toast.makeText(SIPCalculatorActivity.this, "Please Enter deposit amount", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(rateInterest.getText().toString())) {
                Toast.makeText(SIPCalculatorActivity.this, "Please Enter rate of interest", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(loanTenure.getText().toString())) {
                Toast.makeText(SIPCalculatorActivity.this, "Please Enter loan tenure", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(date.getText().toString())) {
                Toast.makeText(SIPCalculatorActivity.this, "Please Enter investment date", Toast.LENGTH_SHORT).show();
            } else {
                String deposit = depositAmount.getText().toString();
                String rateOfInterest = rateInterest.getText().toString();
                String loanYear = loanTenure.getText().toString();
                investDate = date.getText().toString();

                int finalLoanMonth;
                if (tag.equals("Year")) {
                    finalLoanMonth = Integer.parseInt(loanYear) * 12;
                } else {
                    finalLoanMonth = Integer.parseInt(loanYear);
                }

                double finalDepositAmount = Double.parseDouble(deposit);
                double finalRateOfInterest = Double.parseDouble(rateOfInterest);

                if (finalRateOfInterest > 50) {
                    Toast.makeText(SIPCalculatorActivity.this, "You can't add 50% above interest Rate", Toast.LENGTH_SHORT).show();
                    return;
                }

                int finalLoanYear;
                if (tag.equals("Year")) {
                    finalLoanYear = Integer.parseInt(loanYear);
                } else {
                    finalLoanYear = Integer.parseInt(loanYear) / 12;
                }

                if (tag.equals("Year")) {
                    if (finalLoanYear > 30) {
                        Toast.makeText(SIPCalculatorActivity.this, "You can't add 30 year above loan tenure", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (finalLoanMonth > 360) {
                        Toast.makeText(SIPCalculatorActivity.this, "You can't add 360 month above loan tenure", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                SIPFutureValue = calculateSIPFutureValue(finalDepositAmount, finalRateOfInterest, finalLoanMonth);

                investmentAmountSIP = finalDepositAmount * finalLoanMonth;
                interestAmountSIP = SIPFutureValue - investmentAmountSIP;
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
                calendar.add(Calendar.YEAR, finalLoanYear);
                Date maturityDate1 = calendar.getTime();
                finalMaturityDate = dateFormat.format(maturityDate1);

                linear.setVisibility(View.VISIBLE);

                totalInvestmentAmount.setText(df.format(investmentAmountSIP));
                totalInterestValue.setText(df.format(interestAmountSIP));
                maturityValue.setText(df.format(SIPFutureValue));
                maturityDate.setText(finalMaturityDate);

                shareResult.setBackgroundResource(R.drawable.border2);
                convertPdf.setBackgroundResource(R.drawable.border2);
                shareResult.setTextColor(Color.WHITE);
                convertPdf.setTextColor(Color.WHITE);
                shareResult.setEnabled(true);
                convertPdf.setEnabled(true);
            }
        });

        shareResult.setOnClickListener(v -> {
            DecimalFormat df = new DecimalFormat("#.##");

            String shareText = "Total Investment Amount : " + df.format(investmentAmountSIP) + "\n" +
                    "Total Interest Amount : " + df.format(interestAmountSIP) + "\n" +
                    "Maturity Value : " + df.format(SIPFutureValue) + "\n" +
                    "Investment Date : " + investDate + "\n" +
                    "Maturity Date : " + finalMaturityDate + "\n";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "SIP Calculation"));
        });

        convertPdf.setOnClickListener(v -> checkPermissions());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            generatePDF();
        } else {
            if (ContextCompat.checkSelfPermission(SIPCalculatorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

        String random = "SIP" + new Random().nextInt(100000) + ".pdf";
        return folderPath + "/" + random;
    }

    private void generatePDF() {
        String pdfPath = Path();
        Uri fileUri = FileProvider.getUriForFile(this, "com.kmsoft.financialcalculator.fileprovider", new File(pdfPath));

        try {
            PdfWriter writer = new PdfWriter(getContentResolver().openOutputStream(fileUri));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("SIP Calculation")
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
            table.addCell(df.format(investmentAmountSIP));
            table.addCell(df.format(interestAmountSIP));
            table.addCell(df.format(SIPFutureValue));
            table.addCell(investDate);
            table.addCell(finalMaturityDate);

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

    public static double calculateSIPFutureValue(double investmentPerPeriod, double rate, int month) {
        double i = rate / 100 / 12;
        double numerator = (Math.pow(1 + i, month) - 1);
        return investmentPerPeriod * numerator * (1 + i) / i;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;

            date.setText(selectedDate);

        }, year, month + 1, day);

        datePickerDialog.show();
    }


    private void init() {
        back = findViewById(R.id.back);
        depositAmount = findViewById(R.id.deposit_amount);
        rateInterest = findViewById(R.id.rate_interest);
        loanTenure = findViewById(R.id.loan_tenure);
        calculate = findViewById(R.id.calculate);
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
    }
}