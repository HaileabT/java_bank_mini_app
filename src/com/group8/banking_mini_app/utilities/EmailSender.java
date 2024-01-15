package com.group8.banking_mini_app.utilities;

import com.group8.banking_mini_app.Models.BankAccount;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Properties;

public class EmailSender {

    static Session session = null;
    static MimeMessage message = null;
    static Properties properties = null;
    public EmailSender(){

    }

    public static void draftEmailAdd(BankAccount ba) throws MessagingException {
        message = null;
        String recipientEmail = ba.getEmailAddress();
        String emailSubject = "You've opened an account successfully!";
        String emailBody = "Hi, " + ba.getFull_name() + " you have successfully opened a bank account. " +
                "Your account number is " + ba.getAccount_Number() + " and your initial balance is "  +
                ba.getBalance() + "0 ETB. Thank You!";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }

    public static void draftEmailWithdrawn(BankAccount ba, float withDrawAmount) throws MessagingException {
        message= null;
        String recipientEmail = ba.getEmailAddress();
        String emailSubject = "You've withdrawn " + withDrawAmount + "Birr from your account!";
        String emailBody = "Hi, " + ba.getFull_name() + " you have successfully withdrawn from the bank account " +
                  ba.getAccount_Number() + " and your new balance is "  +
                ba.getBalance() + "ETB. Thank You!";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }

    public static void draftEmailDeposit(BankAccount ba, float depositAmount) throws MessagingException {
        message = null;
        String recipientEmail = ba.getEmailAddress();
        String emailSubject = "You've deposited " + depositAmount + " Birr in your account!";
        String emailBody = "Hi, " + ba.getFull_name() + " you have successfully deposited into the bank account " +
                ba.getAccount_Number() + " and your new balance is "  +
                ba.getBalance() + "ETB. Thank You!";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }


    public static void draftEmailTransferS(BankAccount sender, BankAccount recipient, float transferAmount) throws MessagingException {
        message = null;
        String senderEmail = sender.getEmailAddress();
        String emailSubject = "You've transferred " + transferAmount + " Birr from your account!";
        String emailBody = "Hi, " + sender.getFull_name() + " you have successfully transferred " + transferAmount + "ETB to the bank account " +
                recipient.getAccount_Number() + " and your new balance is "  +
                sender.getBalance() + "ETB. Thank You!";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(senderEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }

    public static void draftEmailTransferR(BankAccount sender, BankAccount recipient, float transferAmount) throws MessagingException {
        message = null;
        String recipientEmail = recipient.getEmailAddress();
        String emailSubject = "You've received " + transferAmount + " Birr from your account!";
        String emailBody = "Hi, " + recipient.getFull_name() + " you have received " + transferAmount + "ETB from the bank account " +
                sender.getAccount_Number() + " and your new balance is "  +
                recipient.getBalance() + "ETB. Thank You!";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }

    public static void draftUpdateEmail(BankAccount ba, String updatedField, String oldValue) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MessagingException {
        String fieldValue = "get" + updatedField.substring(0, 1).toUpperCase() + updatedField.substring(1);
        Method method = ba.getClass().getMethod(fieldValue);
        String newVal;
        if (method.invoke(ba) instanceof LocalDate){
            newVal = Utilities.getDateString((LocalDate) method.invoke(ba));
        }
        else {
            newVal = (String) method.invoke(ba);
        }
        updatedAccEmailBuilder(ba, updatedField, oldValue, newVal);
    }

    public static void updatedAccEmailBuilder (BankAccount ba, String updatedField, String oldValue, String newVal) throws MessagingException {
        message = null;
        String recipientEmail = ba.getEmailAddress();
        String emailSubject = "You've changed account information";
        String emailBody = "Hi, " + ba.getFull_name() + " you have successfully changed " + updatedField + " from " + oldValue + " to " + newVal + " of bank account " +
                ba.getAccount_Number() + ". ";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }

    public static void draftCloseAccountEmail(BankAccount ba) throws MessagingException {
        message = null;
        String recipientEmail = ba.getEmailAddress();
        String emailSubject = "You've closed your account!";
        String emailBody = "Hi, " + ba.getFull_name() + " you have successfully closed the bank account " +
                ba.getAccount_Number() + ".";
        message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(emailSubject);
        message.setText(emailBody);
    }
    public static void setupEmailServer(BankAccount ba) {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
    }

    public static void sendEmail() throws MessagingException {
        session = null;
        String senderEmail = "Haileabtesfaye11@gmail.com";
        String senderPassword = "jbgx pqvl apzv eyfn";
        String emailHost = "smtp.gmail.com";
        message.setFrom(senderEmail);

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "Haileabtesfaye11@gmail.com", "jbgx pqvl apzv eyfn");
            }
        });

        Transport transport = session.getTransport("smtp");
        transport.connect(emailHost, senderEmail, senderPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        message.setRecipients(Message.RecipientType.TO, "");
    }

    public static void sendEmail(BankAccount ba) throws MessagingException {
        session = null;
        String senderEmail = "Haileabtesfaye11@gmail.com";
        String senderPassword = "jbgx pqvl apzv eyfn";
        String emailHost = "smtp.gmail.com";
        message.setFrom(senderEmail);

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "Haileabtesfaye11@gmail.com", "jbgx pqvl apzv eyfn");
            }
        });

        Transport transport = session.getTransport("smtp");
        transport.connect(emailHost, senderEmail, senderPassword);
        transport.sendMessage(message, new InternetAddress[]{new InternetAddress(ba.getEmailAddress())});
        transport.close();
        message.setRecipients(Message.RecipientType.TO, "");
    }
}

