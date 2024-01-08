package com.group8.banking_mini_app;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.SavingAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import com.group8.banking_mini_app.utilities.EmailSender;
import com.group8.banking_mini_app.utilities.Utilities;

import java.time.LocalDate;
import java.util.Date;


public class BankApplication {
    public static void main(String[] args) {
        DatabaseManipulator dbm = new DatabaseManipulator();
        LocalDate ld = LocalDate.of(2002, 12, 12);
        BankAccount ba = new SavingAccount("Haileab Tesfaye", ld, 18, "+251903661500", "Haileabtesfaye8@gmail.com", 1002.3f);
        DatabaseManipulator.addBankAccount(ba);
        System.out.println(DatabaseManipulator.findAccount("SVA00001"));
    }
}
