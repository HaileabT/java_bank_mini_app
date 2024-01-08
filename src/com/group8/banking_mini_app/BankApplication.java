package com.group8.banking_mini_app;
import java.util.Scanner;

import com.group8.banking_mini_app.Controllers.AccountantController;
import com.group8.banking_mini_app.Controllers.ManagerController;
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
        AccountantController accController=new AccountantController();
        ManagerController mngController=new ManagerController();
        BankApplication bnkApp=new BankApplication();
        Scanner console=new Scanner(System.in);
        int choice;

  do{
     bnkApp.rollChoice();

      choice=console.nextInt();
      switch (choice){
          case 1:{
             mngController.managerChoice();
              break;
          }
          case 2:{
              accController.accountantChoice();
              break;
          }

      }
  }while(choice!=3);

    }
    public void rollChoice(){
        System.out.println("1.Manager");
        System.out.println("2.Accountant");
        System.out.println("3.Exit");
        System.out.println("Choose Your Position");
    }
}
