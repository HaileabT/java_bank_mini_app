package com.group8.banking_mini_app.Controllers;
import java.time.LocalDate;
import java.util.Scanner;

import com.group8.banking_mini_app.BankApplication;
import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import com.group8.banking_mini_app.utilities.Utilities;


public class ManagerController {
    Scanner console = new Scanner(System.in);

    public void managerChoice() {
        int choice;
        do {
            System.out.println("1.Close Account");
            System.out.println("2.Search Account");
            System.out.println("3.Update Account Information");
            System.out.println("4.Back");
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
                    this.closeAccount();
                    break;
                }
                case 2: {
                    this.searchAccount();
                    break;
                }
                case 3: {
                    this.updateAccountInfo();
                    break;
                }
            }
        } while (choice != 4);
    }


    public void updateAccountInfo() {
        String accountNumber;
        BankAccount finder = null;
        int updateChoice;
        do {
        System.out.println("Enter account number you want to update");
        accountNumber = console.nextLine();
        } while (DatabaseManipulator.findAccount(accountNumber, finder) == null);
        System.out.println("1.Update Name");
        System.out.println("2.Update Date of Birth");
        System.out.println("3.Update Phone Number");
        System.out.println("4.Update Email");
        System.out.println("5.Back");
        do {
            try {
                System.out.print("> ");
                updateChoice = console.nextInt();
            } catch (Exception err){
                System.out.println("Something is wrong with your input please enter numbers between 1 and 5");
                updateChoice = 0;
            }
            if (updateChoice == 5){
                return;
            }
            if (updateChoice > 5 || updateChoice < 1){
                System.out.println(updateChoice + "is not a valid choice!");
            }
            console.nextLine();
        }while(updateChoice > 5 || updateChoice < 1);

        switch(updateChoice){
            case 1:
                String fullName;
                do {
                    System.out.println("Full Name");
                    fullName = console.nextLine();
                } while (!Utilities.isValidName(fullName));
                fullName = Utilities.formalizeName(fullName);
                DatabaseManipulator.updateInfoHandler(accountNumber, "full_name", fullName);
                break;
            case 2:
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
                DatabaseManipulator.updateInfoHandler(accountNumber, "birth_date", ld);
                break;
            case 3:
                String phoneNumber;
                do {
                    System.out.println("Phone Number");
                    System.out.print("+251 ");
                    phoneNumber = console.nextLine();
                } while (!Utilities.isValidPhoneNum(phoneNumber));
                DatabaseManipulator.updateInfoHandler(accountNumber, "phoneNumber", phoneNumber);
                break;
            case 4:
                String emailAddress;
                do {
                    System.out.println("Email Address");
                    emailAddress = console.nextLine();
                } while (!Utilities.isValidEmail(emailAddress));
                DatabaseManipulator.updateInfoHandler(accountNumber, "emailAddress", emailAddress);
                break;
        }
    }

    public void closeAccount() {
        String accountNumber;
        System.out.println("Enter account number you want to close");
        accountNumber = console.nextLine();
        DatabaseManipulator.closeAccountHandler(accountNumber);
    }

        public void searchAccount () {
            String accountNumber;
            System.out.println("Enter account number you want to search");
            accountNumber = console.nextLine();
            BankAccount finder = null;
            finder = DatabaseManipulator.findAccount(accountNumber, finder);
            if (finder != null){
                finder.printAccountDetails();
            }
        }
}
