package com.group8.banking_mini_app.Controllers;
import java.util.Scanner;
import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.InterestFreeAccount;
import com.group8.banking_mini_app.Models.SavingAccount;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import com.group8.banking_mini_app.utilities.Utilities;

import java.time.LocalDate;
import java.util.Scanner;

public class ManagerController {
    Scanner console=new Scanner(System.in);

    public void managerChoice()
    {
       int choice;
       do{
           System.out.println("1.Create Account");
           System.out.println("2.Close Account");
           System.out.println("3.Search Account");
           System.out.println("4.Back");
           System.out.print(">");
           choice=console.nextInt();
           console.next();
           switch (choice){
               case 1:{
                   this.createAccount();
                   break;
               }
               case 2:{
                   this.closeAccount();
                   break;
               }
               case 3:{
                   this.searchAccount();
                   break;
               }
           }
       }while(choice!=4);
    }
    public void AccountType()
    {
        System.out.println("1.Saving Account");
        System.out.println("2.Checking Account");
        System.out.println("3.Non-Interest Account");
        System.out.println("4.Back");
    }

    public void closeAccount()
    {
        String accountNumber;
        System.out.println("Enter Account Number You want to close");
        accountNumber=console.nextLine();
       DatabaseManipulator.closeAccountHandler(accountNumber);
    }
    public void createAccount()
    {
        System.out.println("Welcome to Saving Account Spot Enter the following information defined below");

        String fullName;
        do{
            System.out.println("Full Name");
            fullName=console.nextLine();

        }while(Utilities.isValidName(fullName));

        int birthDay,birthMonth,birthYear;
        LocalDate ld;

       do {
            System.out.println("Birth Day");
            birthDay=console.nextInt();
            System.out.println("Birth Month");
            birthMonth=console.nextInt();
            System.out.println("Birth Year");
            birthYear=console.nextInt();
            ld=LocalDate.of(birthYear,birthMonth,birthDay);
        } while(Utilities.isValidDate(birthYear,birthMonth,birthDay));

        int age;
        do{
            System.out.println("age");
            age=console.nextInt();
            console.nextLine();
        }while(Utilities.isValidAge(age));
        String phoneNumber;
      do{
          System.out.println("Phone Number");
          phoneNumber=console.nextLine();
      }while (Utilities.isValidPhoneNum(phoneNumber));
        String emailAddress;
      do{
          System.out.println("Email Address");
          emailAddress=console.nextLine();
      }while (Utilities.isValidEmail(emailAddress));
        float initBalance;
       do{
               System.out.println("Starting Balance > 50 Birr");
               initBalance= console.nextFloat();
       }while (initBalance<50);


        int choice;
        do{
            this.AccountType();
            choice=console.nextInt();
            switch (choice){
                case 1:{
                    BankAccount savingBnk=new SavingAccount("Some One",fullName,ld,age,phoneNumber,emailAddress,initBalance);
                    break;
                }
                case 2:{
                    BankAccount checkingBnk=new CheckingAccount("Some One",fullName,ld,age,phoneNumber,emailAddress,initBalance);
                    break;
                }
                case 3:{
                    BankAccount interestFreeBnk=new InterestFreeAccount("Some One",fullName,ld,age,phoneNumber,emailAddress,initBalance);
                break;
                }
            }
        }while(choice!=4);
    }
    public void searchAccount(){
        String accountNumber;
        System.out.println("Enter Account Number You want to search");
        accountNumber=console.nextLine();
        DatabaseManipulator.findAccount(accountNumber);
    }
}
