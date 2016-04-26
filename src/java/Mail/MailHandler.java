/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mail;

import DAL.LoanDAO;
import DTO.LoanDTO;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author mqj104
 */
public class MailHandler implements Runnable {

    private int msPerDay = 86400 * 1000;
    private Connection conn;

    public MailHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            try {               
                checkLoans();
                Thread.sleep(msPerDay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void checkLoans() throws InterruptedException {
        LoanDTO[] loans = new LoanDAO(conn).getLoans();
        Date curDate = new Date();
        for (LoanDTO loan : loans) {
            if ((int) ((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay) < 7) {
                String subject = loan.getBarcode() + " is due in less than 7 days!";
                SendEmail(subject, "You have a delivery due in less than 7 days", 
                        "mailservicemis@gmail.com");
                Thread.sleep(20);
            }
        }
    }

    private void SendEmail(String subject, String body, String user) {

        String to = "mailservicemis@gmail.com";
        
        //String to = user + "@student.dtu.dk";

        String from = "mailservicemis@gmail.com";

        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("smtp.gmail.com", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent mail to " + user + " successfully");
        } catch (MessagingException mex) {
            System.out.println("Sent mail to " + user + " failed\n" + mex.getMessage());
        }
    }
}
