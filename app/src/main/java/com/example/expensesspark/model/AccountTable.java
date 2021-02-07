package com.example.expensesspark.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AccountTable extends RealmObject {

    @PrimaryKey
    long accountId;
    String accountName;
    String accountNumber;
    String accountType;
    String currencyType;
    double balance;
    String dateTime;


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public long getaccountId() {
        return accountId;
    }

    public void setaccountId(long accountId) {
        this.accountId = accountId;
    }


}
