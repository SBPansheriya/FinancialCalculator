package com.kmsoft.financialcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kmsoft.financialcalculator.Model.EMI;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "FinancialData";

    private static final String TABLE = "EMICalculation";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "principal_amount";
    private static final String COLUMN_INTEREST_RATE = "interest_rate";
    private static final String COLUMN_LOAN_TENURE = "loan_tenure";
    private static final String COLUMN_TOTAL_INTEREST_AMOUNT = "total_interest_amount";
    private static final String COLUMN_MONTHLY_EMI = "monthly_emi";
    private static final String COLUMN_TOTAL_PAYMENT = "total_payment";
    private static final String COLUMN_DATE = "date";

    private static final int VER = 1;

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_INTEREST_RATE + " TEXT,"
            + COLUMN_LOAN_TENURE + " TEXT,"
            + COLUMN_TOTAL_INTEREST_AMOUNT + " TEXT,"
            + COLUMN_MONTHLY_EMI + " TEXT,"
            + COLUMN_TOTAL_PAYMENT + " TEXT,"
            + COLUMN_DATE + " TEXT"
            + ")";

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
        Log.d("TTT", "DataBase: create database");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("TTT", "onCreate: create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // Insert Data
    public void insertData(EMI emi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_AMOUNT, emi.getPrincipalAmount());
        contentValues.put(COLUMN_INTEREST_RATE, emi.getInterestRate());
        contentValues.put(COLUMN_LOAN_TENURE, emi.getLoanTenure());
        contentValues.put(COLUMN_TOTAL_INTEREST_AMOUNT, emi.getTotalInterestAmount());
        contentValues.put(COLUMN_MONTHLY_EMI, emi.getMonthlyEmi());
        contentValues.put(COLUMN_TOTAL_PAYMENT, emi.getTotalPayment());
        contentValues.put(COLUMN_DATE, emi.getDateTimeStamp());
        db.insert(TABLE, null, contentValues);
        db.close();
    }

    // Delete Data
    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }

    // Retrieve All Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }
}
