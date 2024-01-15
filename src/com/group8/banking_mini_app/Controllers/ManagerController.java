package com.group8.banking_mini_app.Controllers;
import java.util.Scanner;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;


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
                    this.updateAccountInfo();
                    break;
                }
                case 3: {
                    this.searchAccount();
                    break;
                }
            }
        } while (choice != 4);
    }


    public void updateAccountInfo() {

    }

    public void closeAccount() {
        String accountNumber;
        System.out.println("Enter Account Number You want to close");
        accountNumber = console.nextLine();
        DatabaseManipulator.closeAccountHandler(accountNumber);
        System.out.println(accountNumber);
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
