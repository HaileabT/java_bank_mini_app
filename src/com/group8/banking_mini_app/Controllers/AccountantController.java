package com.group8.banking_mini_app.Controllers;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.InterestFreeAccount;
import com.group8.banking_mini_app.Models.SavingAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import com.group8.banking_mini_app.utilities.Utilities;

import java.time.LocalDate;
import java.util.Scanner;

public class AccountantController {
    Scanner console = new Scanner(System.in);
    public void accountantChoice(){
        int choice;
        do{
            System.out.println("1.Create Account");
            System.out.println("2.Deposit");
            System.out.println("3.Transfer");
            System.out.println("4.Withdraw");
            System.out.println("5.Print");
            System.out.println("6.Back");
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
            switch (choice){
                case 1:{
                    this.createAccount();
                    break;
                }
                case 2:{
                    this.deposit();
                    break;
                }
                case 3:{
                    this.transfer();
                    break;
                }
                case 4:{
                    this.withdraw();
                    break;
                }
                case 5:{
                    this.searchAccount();
                    break;
                }
            }
        }while(choice!=6);
    }

    public void createAccount() {
        System.out.println("Enter the following information defined below");

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
        fullName = Utilities.formalizeName(fullName);
        int birthDay, birthMonth, birthYear;
        LocalDate ld;
        do {
            try{
                System.out.println("Birth Day");
                birthDay = console.nextInt();
                System.out.println("Birth Month");
                birthMonth = console.nextInt();
                System.out.println("Birth Year");
                birthYear = console.nextInt();
                ld = LocalDate.of(birthYear, birthMonth, birthDay);
            } catch (Exception err){
                System.out.println("Wrong day, month or year double check and re-enter");
                birthDay = 0;
                birthMonth = 0;
                birthYear = LocalDate.now().getYear() - 2;
                ld = null;
            }
            console.nextLine();
        } while (!Utilities.isValidDate(birthYear, birthMonth, birthDay));
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
                BankAccount savingBnk = new SavingAccount("Some One", fullName, ld, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(savingBnk);
                break;
            }
            case 2: {
                BankAccount checkingBnk = new CheckingAccount("Some One", fullName, ld, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(checkingBnk);
                break;
            }
            case 3: {
                BankAccount interestFreeBnk = new InterestFreeAccount("Some One", fullName, ld, phoneNumber, emailAddress, initBalance, false);
                DatabaseManipulator.addBankAccount(interestFreeBnk);
                break;
            }
        }
    }


    public void AccountType() {
        System.out.println("1.Saving Account");
        System.out.println("2.Checking Account");
        System.out.println("3.Non-Interest Account");
//        System.out.println("4.Back");
    }



    public void deposit()
    {
        String accountNumber;
        Scanner console=new Scanner(System.in);
        System.out.println("Enter account number");
        accountNumber=console.nextLine();
        float amount;
        System.out.println("Enter amount in Birr");
        do{
            try {
                amount = console.nextFloat();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                amount = 0;
            }
            console.nextLine();
        }while(amount <= 0);
        DatabaseManipulator.depositHandler(accountNumber,amount, true);
    }
    public void withdraw()
    {
        String accountNumber;
        Scanner console=new Scanner(System.in);
        System.out.println("Enter account number");
        accountNumber=console.nextLine();
        float amount;
        System.out.println("Enter amount in Birr");
        do{
            try {
                amount = console.nextFloat();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                amount = 0;
            }
            console.nextLine();
        }while(amount <= 0);
        DatabaseManipulator.withdrawHandler(accountNumber,amount, true);
    }
    public void transfer()
    {
        String senderAccountNumber,receiverAccountNumber;
        Scanner console=new Scanner(System.in);
        System.out.println("Enter sender account number");
        senderAccountNumber=console.nextLine();
        System.out.println("Enter receiver account number");
        receiverAccountNumber=console.nextLine();
        float amount;
        do{
            try {
                amount = console.nextFloat();
            } catch (Exception err){
                System.out.println("Enter valid input please");
                amount = 0;
            }
            console.nextLine();
        }while(amount <= 0);
        DatabaseManipulator.transferHandler(senderAccountNumber,receiverAccountNumber,amount);
    }

    public void searchAccount(){
        Scanner console=new Scanner(System.in);
        String accountNumber;
        System.out.println("Enter account number you want to search");
        accountNumber=console.nextLine();
        BankAccount finder = null;
        finder = DatabaseManipulator.findAccount(accountNumber, finder);
        if (finder != null){
            finder.printAccountDetails();
        }
    }
}
