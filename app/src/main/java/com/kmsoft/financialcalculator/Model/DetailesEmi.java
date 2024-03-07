package com.kmsoft.financialcalculator.Model;

public class DetailesEmi {
    double month;
    double principal;
    double interest;
    double totalBalance;

    public DetailesEmi(double month, double principal, double interest, double totalBalance) {
        this.month = month;
        this.principal = principal;
        this.interest = interest;
        this.totalBalance = totalBalance;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }
}
