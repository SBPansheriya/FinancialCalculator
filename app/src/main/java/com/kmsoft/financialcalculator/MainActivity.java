package com.kmsoft.financialcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kmsoft.financialcalculator.Adapter.DrawerAdapter;
import com.kmsoft.financialcalculator.BankingCalculation.CurrencyConversionActivity;
import com.kmsoft.financialcalculator.BankingCalculation.FDCalculatorActivity;
import com.kmsoft.financialcalculator.BankingCalculation.PPFCalculatorActivity;
import com.kmsoft.financialcalculator.BankingCalculation.RDCalculatorActivity;
import com.kmsoft.financialcalculator.BankingCalculation.SimpleAndCompoundActivity;
import com.kmsoft.financialcalculator.EMICalculators.CompareLoanActivity;
import com.kmsoft.financialcalculator.EMICalculators.EMICalculatorActivity;
import com.kmsoft.financialcalculator.Fragment.AboutFragment;
import com.kmsoft.financialcalculator.Fragment.HistoryFragment;
import com.kmsoft.financialcalculator.Fragment.PrivacyPolicyFragment;
import com.kmsoft.financialcalculator.GSTAndVAT.GSTCalculatorActivity;
import com.kmsoft.financialcalculator.GSTAndVAT.VATCalculatorActivity;
import com.kmsoft.financialcalculator.LoanCalculators.LoanAmountActivity;
import com.kmsoft.financialcalculator.LoanCalculators.LoanRateActivity;
import com.kmsoft.financialcalculator.LoanCalculators.LoanTenureActivity;
import com.kmsoft.financialcalculator.Model.DrawerItem;
import com.kmsoft.financialcalculator.MutualFundsAndSIP.EquitySavingSchemeActivity;
import com.kmsoft.financialcalculator.MutualFundsAndSIP.LumpSumCalculatorActivity;
import com.kmsoft.financialcalculator.MutualFundsAndSIP.SIPCalculatorActivity;
import com.kmsoft.financialcalculator.MutualFundsAndSIP.SWPCalculatorActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.DrawerItemClickedListener {

    ImageView emiCalculator, compareLoan, loanAmount, loanTenure, loanRate, currencyConversion, fdCal, rdCal, ppfCal, siCal, sip, swp, lumpSum, elss, gst, vat, homeThree;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    DrawerAdapter mDrawerAdapter;
    List<DrawerItem> mDrawerItems = new ArrayList<>();
    public static boolean isStep = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.rgb(13, 91, 104));

        init();

        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_History), R.drawable.history));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_Share), R.drawable.share));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_Rate), R.drawable.rate));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_Feedback), R.drawable.feedback));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_Privacy_Policy), R.drawable.privacypolicy));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_About), R.drawable.about));
        mDrawerItems.add(new DrawerItem(getString(R.string.drawer_item_Dark_mode), R.drawable.dark_mode));

        mDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDrawerAdapter = new DrawerAdapter(this, mDrawerItems, this);
        mDrawerRecyclerView.setAdapter(mDrawerAdapter);

        View headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, mDrawerRecyclerView, false);
        mDrawerAdapter.addHeaderView(headerView);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        homeThree.setOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));

        emiCalculator.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EMICalculatorActivity.class);
            startActivity(intent);
        });

        compareLoan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CompareLoanActivity.class);
            startActivity(intent);
        });

        loanAmount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoanAmountActivity.class);
            startActivity(intent);
        });

        loanTenure.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoanTenureActivity.class);
            startActivity(intent);
        });

        loanRate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoanRateActivity.class);
            startActivity(intent);
        });

        currencyConversion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CurrencyConversionActivity.class);
            startActivity(intent);
        });

        fdCal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FDCalculatorActivity.class);
            startActivity(intent);
        });

        rdCal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RDCalculatorActivity.class);
            startActivity(intent);
        });

        ppfCal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PPFCalculatorActivity.class);
            startActivity(intent);
        });

        siCal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimpleAndCompoundActivity.class);
            startActivity(intent);
        });

        sip.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SIPCalculatorActivity.class);
            startActivity(intent);
        });

        swp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SWPCalculatorActivity.class);
            startActivity(intent);
        });

        lumpSum.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LumpSumCalculatorActivity.class);
            startActivity(intent);
        });

        elss.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EquitySavingSchemeActivity.class);
            startActivity(intent);
        });

        gst.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GSTCalculatorActivity.class);
            startActivity(intent);
        });

        vat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VATCalculatorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(DrawerItem drawerItem, int position) {

        mDrawerAdapter.setSelectedItemPosition(position + 1);

        if (drawerItem.getName().equals("History")) {
            addFragment(new HistoryFragment());
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerItem.getName().equals("Share This App")) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerItem.getName().equals("Rate This App")) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerItem.getName().equals("Feedback")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"testkmsoft@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about application");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerItem.getName().equals("Privacy Policy")) {
            addFragment(new PrivacyPolicyFragment());
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerItem.getName().equals("About")) {
            addFragment(new AboutFragment());
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drawer_layout);
        if (fragment instanceof HistoryFragment) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (fragment instanceof AboutFragment) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (fragment instanceof PrivacyPolicyFragment) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            MainActivity.this.finish();
            System.exit(0);
        }
    }

    private void addFragment(Fragment Fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_layout, Fragment);
        transaction.commit();
    }

    private void init() {
        emiCalculator = findViewById(R.id.emi_calculator);
        compareLoan = findViewById(R.id.compare_loan);
        loanAmount = findViewById(R.id.loan_amount);
        loanTenure = findViewById(R.id.loan_tenure);
        loanRate = findViewById(R.id.loan_rate);
        currencyConversion = findViewById(R.id.currency_conversion);
        fdCal = findViewById(R.id.fd_cal);
        rdCal = findViewById(R.id.rd_cal);
        ppfCal = findViewById(R.id.ppf_cal);
        siCal = findViewById(R.id.si_cal);
        swp = findViewById(R.id.swp);
        sip = findViewById(R.id.sip);
        lumpSum = findViewById(R.id.lumpsum);
        elss = findViewById(R.id.elss);
        gst = findViewById(R.id.gst);
        vat = findViewById(R.id.vat);
        homeThree = findViewById(R.id.home_three);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerRecyclerView = findViewById(R.id.drawer_list);
    }
}