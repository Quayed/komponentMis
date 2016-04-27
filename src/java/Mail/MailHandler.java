/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mail;

import DAL.ComponentDAO;
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

    private final int msPerDay = 86400 * 1000;
    private final Connection conn;

    public MailHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            try {
                checkLoans();
                Thread.sleep(msPerDay/4);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkLoans() throws InterruptedException {
        LoanDTO[] loans = new LoanDAO(conn).getLoans();
        
        try {
            for (LoanDTO loan : loans) {
                    Date curDate = new Date();
                    if ( (((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) < 7) && (loan.getDeliveryDate() == null || loan.getDeliveryDate().equals(""))) {
                        if ((((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) <= 0) && loan.getMailCount() == 1) {
                        String subject = "Komponent:" + loan.getBarcode() + "-StudieNr:" + loan.getStudentId();
      
                        String body = "Dette er en automatisk påmindelse til " + loan.getStudentId() 
                                + ".\n\nDu har overskredet afleveringsfristen for komponenten " + loan.getComponent().getComponentGroup().getName() + ". Du skal hurtigst muligt aflevere den i Komponentshoppen på DTU Ballerup Campus."                          
                                + " Afleveringsdatoen for komponenten var: " + loan.getDueDate() 
                                + "\n\nMed venlig hilsen\nKomponentshoppen på DTU Ballerup Campus\n"
                                + "\n\n\n***Dette er en autogenereret e-mail. E-mails sendt til denne adresse vil ikke blive besvaret***";
                        
                        SendEmail(subject, body, loan.getStudentId());
                        }
                        else if (loan.getMailCount() == 0){
                                                        
                        String subject = "Komponent:" + loan.getBarcode() + "-StudieNr:" + loan.getStudentId();
                        
                        String body = "Dette er en automatisk påmindelse til " + loan.getStudentId() 
                                + ".\n\nDu har komponenten " + loan.getComponent().getComponentGroup().getName() + ". Du skal inden " 
                                + (int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))
                                + " dage aflevere den eller henvende dig i Komponentshoppen på DTU Ballerup Campus og forlænge udlånet."                          
                                + " Afleveringsdatoen for komponenten er: " + loan.getDueDate() 
                                + "\n\nMed venlig hilsen\nKomponentshoppen på DTU Ballerup Campus\n"
                                + "\n\n\n***Dette er en autogenereret e-mail. E-mails sendt til denne adresse vil ikke blive besvaret***";
                        
                        SendEmail(subject, body, loan.getStudentId());                            
                        }
                    }
            }
        } catch (NullPointerException ex) {
            System.out.println("No due loans found");
            ex.printStackTrace();
        }
    }

    private void SendEmail(String subject, String body, String user) throws InterruptedException {

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
