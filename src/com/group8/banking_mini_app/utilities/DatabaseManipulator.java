package com.group8.banking_mini_app.utilities;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.SavingAccount;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;

public class DatabaseManipulator {
    static BankAccount returnValue = null;
    static BankAccount recipientAcc = null;
    static BankAccount temp = null;
    static Connection bank_database;
    public DatabaseManipulator() {
        try {
            bank_database = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "Haile@mysql11");
            System.out.println("Database connected!");
        }
        catch(Exception err){
            System.out.println("Something went wrong while trying to connect to database!");
            err.printStackTrace();
        }
    }

    public static boolean addBankAccount(BankAccount bankAccount) {
        String addQuery = "insert into bank_account (account_number, account_holder, age, birth_date, phone_number_251, email_address, account_balance) values ("
               + "\'" + bankAccount.getAccount_Number() + "\', \'"
                + bankAccount.getFull_name() + "\', "
                + bankAccount.getAge() + ", \'"
                + Utilities.getDateString(bankAccount.getBirth_date()) + "\', \'"
                + bankAccount.getPhoneNumber() + "\', \'"
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


    public static BankAccount findAccount(String account_number, BankAccount bacc){
            bacc = null;
            try {
                Statement addBankAccount = bank_database.createStatement();
                ResultSet rs = addBankAccount.executeQuery("select * from bank_account where account_number = "  + "\"" + account_number + "\"");
                LocalDate ld;
                String tel;
                if (rs.next()){
                    java.sql.Date bd = rs.getDate("birth_date");
                    ld = bd.toLocalDate();

                    tel = Utilities.constructPhoneNum(rs.getString("phone_number_251"));
                    if (rs.getString("account_number").contains("CHA")){
                        bacc = new CheckingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance"), true
                        );
                    }
                    else if (rs.getString("account_number").contains("SVA")){
                        bacc = new SavingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance"), true
                        );
                    }
                    else {
                        bacc = new SavingAccount(
                                rs.getString("account_number"),
                                rs.getString("account_holder"),
                                ld,
                                tel,
                                rs.getString("email_address"),
                                rs.getFloat("account_balance"), true
                        );
                    }
                }


                if (bacc != null){
                    bacc.setAccount_Number(rs.getString("account_number"));
                    return bacc;
                }
                System.out.println("Couldn't find the account provided!");
                return null;
            }
            catch (Exception err){
                err.printStackTrace();
                return null;
            }

    }

    public static boolean depositHandler(String ba, float deposit_amount, boolean sendEmail){
        float newBalance;
        BankAccount depositor = null;
        depositor = findAccount(ba, depositor);
        if (depositor != null){
            try {
                newBalance = deposit_amount + depositor.getBalance();
                depositor.setBalance(newBalance);
            Statement addBankAccount = bank_database.createStatement();
            int rs = addBankAccount.executeUpdate(
                    "update bank_account set account_balance = " + newBalance +
                            "where account_number = " + "\"" + ba + "\"");
            if (sendEmail){
            EmailSender.setupEmailServer(depositor);
            EmailSender.draftEmailDeposit(depositor, deposit_amount);
            EmailSender.sendEmail();
            }
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

    public static boolean withdrawHandler(String ba, float withdraw_amount, boolean sendEmail){
        float newBalance;
        BankAccount withdrawer = null;
        withdrawer = findAccount(ba, withdrawer);
        if (withdrawer != null){
            try {
                if (withdrawer instanceof CheckingAccount) {
                    if (withdrawer.getBalance() - withdraw_amount < CheckingAccount.getOverDraft()) {
                        System.out.println("Overdraft limit reached!");
                        return false;
                    }
                }
                else {
                if (withdraw_amount > withdrawer.getBalance() || withdrawer.getBalance() == 0){
                    System.out.println("Insufficient Funds!");
                    return false;
                }
                }
                newBalance = withdrawer.getBalance() - withdraw_amount;
                withdrawer.setBalance(newBalance);
                Statement addBankAccount = bank_database.createStatement();
                int rs = addBankAccount.executeUpdate(
                        "update bank_account set account_balance = " + newBalance +
                                "where account_number = " + "\"" + ba + "\"");
                if (sendEmail){
                EmailSender.setupEmailServer(withdrawer);
                EmailSender.draftEmailWithdrawn(withdrawer, withdraw_amount);
                EmailSender.sendEmail();
                }
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
        BankAccount transferee = null;
        transferee = findAccount(sender, transferee);
        BankAccount rcpt = null;
        rcpt = findAccount(recipient, rcpt);
        if (transferee != null && rcpt != null){
            try {
                if (transferee instanceof CheckingAccount) {
                    if (transferee.getBalance() - transferAmount < CheckingAccount.getOverDraft()) {
                        System.out.println("Overdraft limit reached!");
                        return false;
                    }
                }
                else {
                if (transferAmount > transferee.getBalance() || transferee.getBalance() <= 0){
                    System.out.println("Insufficient Funds!");
                    return false;
                }
                }
                newBalance = transferee.getBalance() - transferAmount;
                transferee.setBalance(newBalance);
                newBalRec = transferAmount + rcpt.getBalance();
                rcpt.setBalance(newBalRec);
                withdrawHandler(sender, transferAmount, false);
                depositHandler(recipient, transferAmount,false);
                EmailSender.setupEmailServer(transferee);
                EmailSender.draftEmailTransferS(transferee, rcpt, transferAmount);
                EmailSender.sendEmail();
                EmailSender.setupEmailServer(rcpt);
                EmailSender.draftEmailTransferR(transferee, rcpt, transferAmount);
                EmailSender.sendEmail();
                System.out.println("");
                System.out.println("The transfer was successful!");
                System.out.println("Sender: ");
                transferee.printAccountDetails();
                System.out.println("\n");
                System.out.println("Receiver: ");
                rcpt.printAccountDetails();
                System.out.println("\n\n");
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

    public static boolean updateInfoHandler(String accNum, String field, String value){
        String sqlField;
        if (field.equals("full_name")){
            sqlField = "account_holder";
        }
        else if (field.equals("phoneNumber")){
            sqlField = "phone_number_251";
        }
        else if (field.equals("emailAddress")){
            sqlField = "email_address";
        }
        else {
            sqlField = "none";
        }
        String old;
        BankAccount accUpdater = null;
        accUpdater = findAccount(accNum, accUpdater);
        if (accUpdater != null){
            try {
                String fieldValue = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                String fieldValueSt = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
                Method method = accUpdater.getClass().getMethod(fieldValue);
                Method methods = accUpdater.getClass().getMethod(fieldValueSt, String.class);
                old = (String) method.invoke(accUpdater);
                System.out.println(old);
                methods.invoke(accUpdater, value);
                Statement addBankAccount = bank_database.createStatement();
                String statement = "update bank_account set " + sqlField + " = \"" + value +
                        "\" where account_number = " + "\"" + accUpdater.getAccount_Number() + "\";";
                int rs = addBankAccount.executeUpdate(statement);
                EmailSender.setupEmailServer(accUpdater);
                EmailSender.draftUpdateEmail(accUpdater, field, old);
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
        BankAccount accUpdater = null;
        accUpdater = findAccount(accNum, accUpdater);
        if (accUpdater != null){
            try {
                String fieldValue = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                String fieldValueSt = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
                Method method = accUpdater.getClass().getMethod(fieldValue);
                Method methods = accUpdater.getClass().getMethod(fieldValueSt, LocalDate.class);
                LocalDate ld = (LocalDate) method.invoke(accUpdater);
                old = Utilities.getDateString(ld);
                methods.invoke(accUpdater, value);
                Statement addBankAccount = bank_database.createStatement();
                int rs = addBankAccount.executeUpdate(
                        "update bank_account set " +  field + " = \"" + value +
                                "\" where account_number = " + "\"" + accUpdater.getAccount_Number() + "\"");
                addBankAccount.executeUpdate("update bank_account set age = " + (LocalDate.now().getYear() - value.getYear()) +
                        " where account_number = " + "\"" + accUpdater.getAccount_Number() + "\"");
                String oldString = "" + old;
                EmailSender.setupEmailServer(accUpdater);
                EmailSender.draftUpdateEmail(accUpdater, field, oldString);
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
        BankAccount accUpdater = null;
        accUpdater = findAccount(accNum, accUpdater);
        if (accUpdater != null){
            try {
                if (accUpdater.getBalance() >= 20.0f){
                    System.out.println("The account still has some balance withdraw first!");
                    return false;
                }
                Statement addBankAccount = bank_database.createStatement();
                int rs = addBankAccount.executeUpdate(
                        "delete from bank_account " +
                                "where account_number = " + "\"" + accNum + "\"");

                EmailSender.setupEmailServer(accUpdater);
                EmailSender.draftCloseAccountEmail(accUpdater);
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
            int rs = addBankAccount.executeUpdate("update bank_account set account_balance = 0.07 * account_balance + account_balance where not account_number like 'IFA%' ");
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
