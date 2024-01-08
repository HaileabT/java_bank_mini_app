package com.group8.banking_mini_app.Models;

import com.group8.banking_mini_app.utilities.Utilities;

import java.time.LocalDate;
import java.util.Date;

public class InterestFreeAccount extends BankAccount{

    private static String acc_type = "IFA";

    public InterestFreeAccount(String account_number, String fullName, LocalDate birthDay, int age, String phoneNumber, String emailAddress, float initBalance){
        super(account_number, fullName, birthDay, age, phoneNumber, emailAddress, initBalance);

        setAccount_Number(Utilities.generateAccNumber(this));
    }

    public void printAccountDetails(){
        System.out.println("Account Type: Interest Free Account" );
        System.out.println("Account Number: " + account_Number );
        System.out.println("Account Holder: " + full_name);
        System.out.println("Email Address: " + emailAddress);
        System.out.println("--------------------------------------");
        System.out.println("Account Balance: " + balance + "ETB");
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getAccount_Number(){
        return account_Number;
    }

    public void setAccount_Number(String acc_number){
        account_Number = acc_number;
    }

    public static String getAcc_type() {
        return acc_type;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
