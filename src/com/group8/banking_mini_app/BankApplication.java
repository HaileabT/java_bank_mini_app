package com.group8.banking_mini_app;
import java.util.Scanner;

import com.group8.banking_mini_app.Controllers.AccountantController;
import com.group8.banking_mini_app.Controllers.ManagerController;
import com.group8.banking_mini_app.utilities.DatabaseManipulator;
import java.time.LocalDate;


public class BankApplication {

    static void updateInterestHandler(){
        LocalDate date = LocalDate.now();
        if (date.getDayOfMonth() == 1){
        DatabaseManipulator.calculateAndUpdateInterestHandler();
            System.out.println("Interest was add to accounts!");
        }
        else{
            return;
        }
    }
    public static void main(String[] args) {
        AccountantController accController=new AccountantController();
        ManagerController mngController=new ManagerController();
        DatabaseManipulator dbm = new DatabaseManipulator();
        updateInterestHandler();
        BankApplication bnkApp=new BankApplication();
        Scanner console=new Scanner(System.in);
        int choice;

  do{
     bnkApp.rollChoice();

      do{
          try {
              choice = console.nextInt();
          } catch (Exception err){
              System.out.println("Enter valid input please");
              choice = 0;
          }
          console.nextLine();
      }while(choice <= 0);
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
