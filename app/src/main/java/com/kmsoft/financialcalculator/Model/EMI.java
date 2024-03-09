package com.kmsoft.financialcalculator.Model;

import java.io.Serializable;

public class EMI implements Serializable {
    int id;
    double principalAmount;
    double interestRate;
    double loanTenure;
    double totalInterestAmount;
    double monthlyEmi;
    double totalPayment;
    String dateTimeStamp;

    public EMI(int id, double principalAmount, double interestRate, double loanTenure, double totalInterestAmount, double monthlyEmi, double totalPayment,String dateTimeStamp) {
        this.id = id;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.loanTenure = loanTenure;
        this.totalInterestAmount = totalInterestAmount;
        this.monthlyEmi = monthlyEmi;
        this.totalPayment = totalPayment;
        this.dateTimeStamp = dateTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(double loanTenure) {
        this.loanTenure = loanTenure;
    }

    public double getTotalInterestAmount() {
        return totalInterestAmount;
    }

    public double getMonthlyEmi() {
        return monthlyEmi;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }
}
