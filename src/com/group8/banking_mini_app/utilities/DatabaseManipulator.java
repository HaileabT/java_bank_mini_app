package com.group8.banking_mini_app.utilities;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.SavingAccount;

import javax.swing.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;

public class DatabaseManipulator {
    static BankAccount returnValue = null;
    static BankAccount recipientAcc = null;

    static Connection bank_database;
    public DatabaseManipulator() {
        try {
            bank_database = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "Haile@mysql11");
            System.out.println("Connected");
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }

    public static boolean addBankAccount(BankAccount bankAccount) {
        String addQuery = "insert into bank_account (account_number, account_holder, age, birth_date, phone_number_251, email_address, account_balance) values ("
               + "\'" + bankAccount.getAccount_Number() + "\', \'"
                + bankAccount.getFull_name() + "\', "
                + bankAccount.getAge() + ", \'"
                + Utilities.getDateString(bankAccount.getBirth_date()) + "\', \'"
                + bankAccount.getPhoneNumber().substring(4) + "\', \'"
                + bankAccount.getEmailAddress() + "\', "
                + bankAccount.getBalance() + "); ";
        try {
            Statement addBankAccount = bank_database.createStatement();
            int rs = addBankAccount.executeUpdate(addQuery);
            EmailSender.setupEmailServer(bankAccount);
            EmailSender.draftEmailAdd(bankAccount);
            EmailSender.sendEmail();
            System.out.println("Account opened successfully!");
            return true;
        } catch (Exception err){
            err.printStackTrace();
            return false;
        }
    }


    public static boolean findAccount(String account_number){
        returnValue = null;
        try {
        Statement addBankAccount = bank_database.createStatement();
        ResultSet rs = addBankAccount.executeQuery("select * from bank_account where account_number = "  + "\"" + account_number + "\"");
        LocalDate ld;
        String tel;
        if (rs.next()){
            int year = rs.getDate("birth_date").getYear();
            int month = rs.getDate("birth_date").getMonth();
            int day = rs.getDate("birth_date").getDay();
            ld = LocalDate.of(year, month, day);

           tel = Utilities.constructPhoneNum(rs.getString("phone_number_251"));
            if (rs.getString("account_number").contains("CHA")){
                returnValue = new CheckingAccount(
                        rs.getString("account_number"),
                        rs.getString("account_holder"),
                        ld,
                        rs.getInt("age"),
                        tel,
                        rs.getString("email_address"),
                        rs.getFloat("account_balance")
                );
            }
            else if (rs.getString("account_number").contains("SVA")){
                returnValue = new SavingAccount(
                        rs.getString("account_number"),
                        rs.getString("account_holder"),
                        ld,
                        rs.getInt("age"),
                        tel,
                        rs.getString("email_address"),
                        rs.getFloat("account_balance")
                );
            }
            else {
                returnValue = new SavingAccount(
                        rs.getString("account_number"),
                        rs.getString("account_holder"),
                        ld,
                        rs.getInt("age"),
                        tel,
                        rs.getString("email_address"),
                        rs.getFloat("account_balance"));
            }
        }

        if (returnValue != null){
           return true;
        }
            System.out.println("Couldn't find the accounts provided!");
           return false;
        }
        catch (Exception err){
            err.printStackTrace();
            return  false;
        }
    }

    public static boolean findAccount(String account_number, Boolean recipient){
        if (recipient){
            recipientAcc = null;
            try {
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery("select * from bank_account where account_number = "  + "\"" + account_number + "\"");
                LocalDate ld;
                String tel;
                if (rs.next()){
                    int year = rs.getDate("birth_date").getYear();
                    int month = rs.getDate("birth_date").getMonth();
                    int day = rs.getDate("birth_date").getDay();
                    ld = LocalDate.of(year, month, day);

                    tel = Utilities.constructPhoneNum(rs.getString("phone_number_251"));
                    if (rs.getString("account_number").contains("CHA")){
                        recipientAcc = new CheckingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                rs.getInt("age"),
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance")
                        );
                    }
                    else if (rs.getString("account_number").contains("SVA")){
                        recipientAcc = new SavingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                rs.getInt("age"),
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance")
                        );
                    }
                    else {
                        recipientAcc = new SavingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                rs.getInt("age"),
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance"));
                    }
                }

                if (recipientAcc != null){
                    recipientAcc.printAccountDetails();
                    return true;
                }
                System.out.println("Couldn't find the account provided!");
                return false;
            }
            catch (Exception err){
                err.printStackTrace();
                return  false;
            }
        }
        else{
            return false;
        }

    }

    public static boolean depositHandler(String ba, float deposit_amount){
        float newBalance;
        if (findAccount(ba)){
            try {
                newBalance = deposit_amount + returnValue.getBalance();
                returnValue.setBalance(newBalance);
            Statement addBankAccount = bank_database.createStatement();
            ResultSet rs = addBankAccount.executeQuery(
                    "update bank_account set balance = " + newBalance +
                            "where account_number = " + "\"" + ba + "\"");
            EmailSender.setupEmailServer(returnValue);
            EmailSender.draftEmailDeposit(returnValue, deposit_amount);
            EmailSender.sendEmail();
            return true;
            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find the account provided!");
            return false;
        }
    }

    public static boolean withdrawHandler(String ba, float withdraw_amount){
        float newBalance;
        if (findAccount(ba)){
            try {
                if (withdraw_amount > returnValue.getBalance() || returnValue.getBalance() == 0){
                    System.out.println("Insufficient Funds!");
                    return false;
                }
                newBalance = withdraw_amount - returnValue.getBalance();
                returnValue.setBalance(newBalance);
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery(
                        "update bank_account set balance = " + newBalance +
                                "where account_number = " + "\"" + ba + "\"");
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftEmailDeposit(returnValue, withdraw_amount);
                EmailSender.sendEmail();
                return true;
            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find the accounts provided!");
            return false;
        }
    }
    public static boolean transferHandler(String sender, String recipient, float transferAmount){
        float newBalance;
        float newBalRec;
        if (findAccount(sender) && findAccount(recipient, true)){
            try {
                if (transferAmount > returnValue.getBalance() || returnValue.getBalance() <= 0){
                    System.out.println("Insufficient Funds!");
                    return false;
                }
                newBalance = transferAmount - returnValue.getBalance();
                returnValue.setBalance(newBalance);
                newBalRec = transferAmount + recipientAcc.getBalance();
                recipientAcc.setBalance(newBalRec);
                withdrawHandler(sender, transferAmount);
                depositHandler(recipient, transferAmount);
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftEmailTransferS(returnValue, recipientAcc, transferAmount);
                EmailSender.sendEmail();
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftEmailTransferR(returnValue, recipientAcc, transferAmount);
                EmailSender.sendEmail();
                System.out.println("The transfer was successful!");
                System.out.println("Sender: ");
                returnValue.printAccountDetails();
                System.out.println("\n");
                System.out.println("Receiver: ");
                recipientAcc.printAccountDetails();
                return true;
            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find the accounts provided!");
            return false;
        }
    }

    public static boolean updateInfoHandler(String accNum, String field, int value){
        int old;
        if (findAccount(accNum)){
            try {
                String fieldValue = "get" + field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase();
                Method method = returnValue.getClass().getMethod(fieldValue);
                old = (int) method.invoke(returnValue);
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery(
                        "update bank_account set field = " + value +
                                "where account_number = " + "\"" + returnValue.getAccount_Number() + "\"");
                String oldString = "" + old;
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftUpdateEmail(returnValue, field, oldString);
                EmailSender.sendEmail();
                System.out.println("Operation successful, check your email!");
                return true;
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find account!");
            return false;
        }
    }



    public static boolean updateInfoHandler(String accNum, String field, String value){
        String old;
        if (findAccount(accNum)){
            try {
                String fieldValue = "get" + field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase();
                Method method = returnValue.getClass().getMethod(fieldValue);
                old = (String) method.invoke(returnValue);
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery(
                        "update bank_account set field = " + value +
                                "where account_number = " + "\"" + returnValue.getAccount_Number() + "\"");
                String oldString = "" + old;
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftUpdateEmail(returnValue, field, oldString);
                EmailSender.sendEmail();
                System.out.println("Operation successful, check your email!");
                return true;
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find account!");
            return false;
        }
    }

    public static boolean updateInfoHandler(String accNum, String field, LocalDate value){
        String old;
        if (findAccount(accNum)){
            try {
                String fieldValue = "get" + field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase();
                Method method = returnValue.getClass().getMethod(fieldValue);
                LocalDate ld = (LocalDate) method.invoke(returnValue);
                old = Utilities.getDateString(ld);
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery(
                        "update bank_account set field = " + value +
                                "where account_number = " + "\"" + returnValue.getAccount_Number() + "\"");
                String oldString = "" + old;
                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftUpdateEmail(returnValue, field, oldString);
                EmailSender.sendEmail();
                System.out.println("Operation successful, check your email!");
                return true;
            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("Couldn't find account!");
            return false;
        }
    }

    public static boolean closeAccountHandler(String accNum){
        if (findAccount(accNum)){
            try {
                if (returnValue.getBalance() <= 20.0f){
                    System.out.println("The account still has some balance withdraw first!");
                    return false;
                }
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery(
                        "delete from bank_account " +
                                "where account_number = " + "\"" + accNum + "\"");

                EmailSender.setupEmailServer(returnValue);
                EmailSender.draftCloseAccountEmail(returnValue);
                EmailSender.sendEmail();
                return true;
            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("No account with such account number!");
            return false;
        }
    }

    public static boolean calculateAndUpdateInterestHandler(){
        try {
            Statement addBankAccount = bank_database.createStatement();
            ResultSet rs = addBankAccount.executeQuery("update bank_account set account_balance = 0.07 * account_balance + account_balance where not account_number like 'IFA%' ");
            System.out.println("Updated account balances successfully!");
            return true;
        } catch (Exception err){
            err.printStackTrace();
            return false;
        }
    }
    public static int latestAccountNumber(String acc_type){
        try {
            Statement getLatest = bank_database.createStatement();
            ResultSet rs = getLatest.executeQuery(
                    "select * from bank_account " +
                            "where account_number LIKE " + "\"" + acc_type + "%\"" +
                            "order by id desc " +
                            "limit 1"
            );

            if (!rs.next()){
                return 0;
            };

            String acc_num = rs.getString("account_number");
            if (acc_num.equals("null")){
                return 0;
            }
            else {
                acc_num = acc_num.substring(6);
                return Integer.parseInt(acc_num);
            }

        } catch (Exception err){
            err.printStackTrace();
            return -1;
        }
    }
}
