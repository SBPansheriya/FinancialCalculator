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

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
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

    public void setTotalInterestAmount(double totalInterestAmount) {
        this.totalInterestAmount = totalInterestAmount;
    }

    public double getMonthlyEmi() {
        return monthlyEmi;
    }

    public void setMonthlyEmi(double monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }
}
