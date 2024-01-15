package com.group8.banking_mini_app.utilities;

import com.group8.banking_mini_app.Models.BankAccount;
import com.group8.banking_mini_app.Models.CheckingAccount;
import com.group8.banking_mini_app.Models.InterestFreeAccount;
import com.group8.banking_mini_app.Models.SavingAccount;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

public class Utilities {

    public static String formalizeName(String name){
        String temp = "";
        String[] holder = name.split(" ");
        for (int i = 0; i< holder.length; i++){
            holder[i] = holder[i].substring(0, 1).toUpperCase() + holder[i].substring(1).toLowerCase();
            temp = temp + holder[i] + " ";
        }
        temp = temp.trim();
        name = temp;
        return name;
    }

    public static boolean isValidName(String fullName) {
        String[] name = fullName.split(" ");
        if (name.length > 2){
            System.out.println("You entered more than two words");
            return false;
        }
        else {
//            return input.matches(".*[^a-zA-Z0-9].*");
            return fullName.matches("^[a-zA-Z ]+$");
            }
        }

        public static boolean isValidDate(int year, int month, int day){
        if (LocalDate.now().getYear() < year){
            System.out.println("Invalid Date");
            return false;
        }
        try {
            LocalDate.of(year, month, day);
            return true;
        }
        catch (Exception err){
            err.printStackTrace();
            return false;
        }
        }

    public static LocalDate constructDate(int day, int month, int year){
        LocalDate finalDate;
        finalDate = LocalDate.of(year, month, day);
        return finalDate;
    }

    public static String constructPhoneNum(String code){
        return "+251" + code;
    }

    private static boolean validatePattern(String someString, String pattern){
        boolean matches;
//        String emailPatternString = "^[a-zA-Z0-9. _-]+@[a-zA-Z0-9. -]+\\. [a-zA-Z]{2,4}$/";
        Pattern patternValid = Pattern.compile(pattern);
        Matcher matcher = patternValid.matcher(someString);
        if (matcher.matches()){
            matches = true;
        }
        else {
            matches = false;
        }
        return matches;
    }

    public static boolean isValidEmail(String email){
        return validatePattern(email, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
    }

    public static boolean isValidPhoneNum(String phoneNum){
        return validatePattern(phoneNum, "^\\d{9}$");
    }

    public static String generateAccNumber(BankAccount ba, boolean temp){
        if (ba instanceof CheckingAccount){
        return CheckingAccount.getAcc_type() + "0000" + (DatabaseManipulator.latestAccountNumber("CHA") + ((temp)? 0 : 1));
        }
        else if (ba instanceof SavingAccount){
            return SavingAccount.getAcc_type() + "0000" + (DatabaseManipulator.latestAccountNumber("SVA") + ((temp)? 0 : 1));
        }
        else if (ba instanceof InterestFreeAccount){
            return InterestFreeAccount.getAcc_type() + "0000" + (DatabaseManipulator.latestAccountNumber(("IFA")) + ((temp)? 0 : 1));
        }
        else {
            return "None";
        }
    }

    public static String getDateString(LocalDate ld){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf.format(ld);
    }

}
