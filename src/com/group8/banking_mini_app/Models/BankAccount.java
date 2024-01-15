package com.group8.banking_mini_app.Models;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.Locale;

public abstract class BankAccount {
    protected String full_name;
    protected int age;
    protected LocalDate birth_date;
    protected String phoneNumber;
    protected String emailAddress;
    protected float balance;
    protected String account_Number;


    public abstract String getAccount_Number();

    private static String acc_type;

    public static String getAcc_type(){
        return acc_type;
    }

    public abstract int getAge();

    public abstract void setAge(int age);

    public BankAccount(){

    }

    public BankAccount(String account_number, String fullName, LocalDate birthDay, String phoneNumber, String emailAddress, float initBalance){
        this.full_name = fullName;
        this.age = LocalDate.now().getYear() - birthDay.getYear();
        this.birth_date = birthDay;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.balance = initBalance;
    }

    public abstract void setAccount_Number(String acc_number);
    public abstract void printAccountDetails();

    public abstract float getBalance();
    public abstract void setBalance(float balance);
    public abstract String getFull_name();

    public abstract void setFull_name(String full_name);

    public abstract LocalDate getBirth_date();

    public abstract void setBirth_date(LocalDate birth_date);

    public abstract String getPhoneNumber();

    public abstract void setPhoneNumber(String phoneNumber);

    public abstract String getEmailAddress();

    public abstract void setEmailAddress(String emailAddress);
}
