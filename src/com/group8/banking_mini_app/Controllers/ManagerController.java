package com.group8.banking_mini_app.Controllers;
import java.util.Scanner;

import com.group8.banking_mini_app.BankApplication;
import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.InterestFreeAccount;
import com.group8.banking_mini_app.Models.SavingAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import com.group8.banking_mini_app.utilities.Utilities;

import java.time.LocalDate;
import java.util.Scanner;

public class ManagerController {
    Scanner console = new Scanner(System.in);

    public void managerChoice() {
        int choice;
        do {
            System.out.println("1.Create Account");
            System.out.println("2.Close Account");
            System.out.println("3.Search Account");
            System.out.println("4.Update Account Information");
            System.out.println("5.Back");
            do{
            try {
            System.out.print(">");
            choice = console.nextInt();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                choice = 0;
            }
            console.nextLine();
            }while(choice <= 0);
            switch (choice) {
                case 1: {
                    this.createAccount();
                    break;
                }
                case 2: {
                    this.closeAccount();
                    break;
                }
                case 3: {
                    this.searchAccount();
                    break;
                }
            }
        } while (choice != 5);
    }

    public void AccountType() {
        System.out.println("1.Saving Account");
        System.out.println("2.Checking Account");
        System.out.println("3.Non-Interest Account");
//        System.out.println("4.Back");
    }

    public void closeAccount() {
        String accountNumber;
        System.out.println("Enter Account Number You want to close");
        accountNumber = console.nextLine();
        DatabaseManipulator.closeAccountHandler(accountNumber);
        System.out.println(accountNumber);
    }

    public void createAccount() {
        System.out.println("Welcome to Saving Account Spot Enter the following information defined below");

        int choice;
        do{
            try {
                this.AccountType();
                choice = console.nextInt();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                choice = 0;
            }
            console.nextLine();
        }while(choice >= 4 || choice <= 0);
        String fullName;
        do {
            System.out.println("Full Name");
            fullName = console.nextLine();

        } while (!Utilities.isValidName(fullName));

        int birthDay, birthMonth, birthYear;
        LocalDate ld;

        do {
            System.out.println("Birth Day");
            birthDay = console.nextInt();
            System.out.println("Birth Month");
            birthMonth = console.nextInt();
            System.out.println("Birth Year");
            birthYear = console.nextInt();
            ld = LocalDate.of(birthYear, birthMonth, birthDay);
        } while (!Utilities.isValidDate(birthYear, birthMonth, birthDay));

        int age;
        do {
            System.out.println("Age");
            try {
            age = console.nextInt();
            } catch (Exception err){
                System.out.println("That is an invalid input make sure to enter a number");
                age = -50;
            }
            console.nextLine();
        } while (!Utilities.isValidAge(age));
        String phoneNumber;
        do {
            System.out.println("Phone Number");
            System.out.print("+251 ");
            phoneNumber = console.nextLine();
        } while (!Utilities.isValidPhoneNum(phoneNumber));
        String emailAddress;
        do {
            System.out.println("Email Address");
            emailAddress = console.nextLine();
        } while (!Utilities.isValidEmail(emailAddress));
        float initBalance;
        do{
            try {
                System.out.println("Starting Balance > 50 Birr");
                initBalance = console.nextFloat();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                initBalance = 0;
            }
            console.nextLine();
        }while(initBalance < 50);


        switch (choice) {
            case 1: {
                BankAccount savingBnk = new SavingAccount("Some One", fullName, ld, age, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(savingBnk);
                break;
            }
            case 2: {
                BankAccount checkingBnk = new CheckingAccount("Some One", fullName, ld, age, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(checkingBnk);
                break;
            }
            case 3: {
                BankAccount interestFreeBnk = new InterestFreeAccount("Some One", fullName, ld, age, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(interestFreeBnk);
                break;
            }
        }
    }
        public void searchAccount () {
            String accountNumber;
            System.out.println("Enter Account Number You want to search");
            accountNumber = console.nextLine();
            BankAccount finder = null;
            finder = DatabaseManipulator.findAccount(accountNumber, finder);
            if (finder != null){
                finder.printAccountDetails();
            }
        }
}
