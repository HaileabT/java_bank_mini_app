package com.group8.banking_mini_app.Controllers;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;

import javax.xml.crypto.Data;
import java.util.Scanner;

public class AccountantController {
    public void accountantChoice(){
        Scanner console=new Scanner(System.in);
        int choice;
        do{
            System.out.println("1.Deposit");
            System.out.println("2.Transfer");
            System.out.println("3.Withdraw");
            System.out.println("4.Print");
            System.out.println("5.Back");
            choice=console.nextInt();
            console.nextLine();
            switch (choice){
                case 1:{
                    this.deposit();
                    break;
                }
                case 2:{
                    this.transfer();
                    break;
                }
                case 3:{
                    this.withdraw();
                    break;
                }
                case 4:{
                    this.searchAccount();
                    break;
                }
            }
        }while(choice!=5);
    }
    public void deposit()
    {
        String accountNumber;
        Scanner console=new Scanner(System.in);
        System.out.println("Enter account number");
        accountNumber=console.nextLine();
        int amount;
        System.out.println("Enter amount in Birr");
        amount=console.nextInt();
        DatabaseManipulator.depositHandler(accountNumber,amount, true);
    }
    public void withdraw()
    {
        String accountNumber;
        Scanner console=new Scanner(System.in);
        System.out.println("Enter account numbers");
        accountNumber=console.nextLine();
        float amount;
        System.out.println("Enter amount in Birr");
        amount=console.nextFloat();
        console.nextLine();
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
        int amount;
        while(true){
            try{
        System.out.println("Enter amount in Birr");
        amount=console.nextInt();
        break;
            } catch(Exception err){
                console.nextLine();
                System.out.println("Something is wrong with your Input try again!");
            }

        }
        DatabaseManipulator.transferHandler(senderAccountNumber,receiverAccountNumber,amount);
    }

    public void searchAccount(){
        Scanner console=new Scanner(System.in);
        String accountNumber;
        System.out.println("Enter Account Number You want to search");
        accountNumber=console.nextLine();
        BankAccount finder = null;
        finder = DatabaseManipulator.findAccount(accountNumber, finder);
        if (finder != null){
            finder.printAccountDetails();
        }
    }
}
