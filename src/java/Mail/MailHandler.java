
package Mail;

import DAL.DatabaseConfig;
import DAL.LoanDAO;
import DTO.LoanDTO;
import java.sql.Connection;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Christian Genter
 */
public class MailHandler implements Runnable {

    private final int msPerDay = 86400 * 1000;
    private final Connection conn;
    private final int checksPerDay = 4;

    public MailHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            
            // Check over due loans "checksPerDay" times every day
            try {
                checkLoans();
                Thread.sleep(msPerDay / checksPerDay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkLoans() throws InterruptedException {
        LoanDTO[] loans = new LoanDAO(conn).getLoans();

        // Check all loans
        try {
            for (LoanDTO loan : loans) {
                
                // Get current date/time
                Date curDate = new Date();
                
                // Check if the loan is over-due in less than 7 days 
                if ((((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) < 7) && (loan.getDeliveryDate() == null || loan.getDeliveryDate().equals(""))) {
                    
                    // Check if the loan is over-due today or tommorow & if the student has not allready been notified by e-mail about it
                    if ((((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) + 1 <= 0) && loan.getMailCount() == 1) {
                        
                        // Make e-mail subject
                        String subject = "Komponent:" + loan.getBarcode() + "-StudieNr:" + loan.getStudentId();
                        
                        // Make e-mail body
                        String body = "Dette er en automatisk påmindelse til " + loan.getStudentId()
                                + ".\n\nDu har overskredet afleveringsfristen for komponenten " + loan.getComponent().getComponentGroup().getName() + ". Du skal hurtigst muligt aflevere den i Komponentshoppen på DTU Ballerup Campus."
                                + " Afleveringsdatoen for komponenten var: " + loan.getDueDate()
                                + "\n\nMed venlig hilsen\nKomponentshoppen på DTU Ballerup Campus\n"
                                + "\n\n\n***Dette er en autogenereret e-mail. E-mails sendt til denne adresse vil ikke blive besvaret***";

                        // Send e-mail to student
                        SendEmail(subject, body, loan.getStudentId());
                        
                        // Set mail count
                        loan.setMailCount(2);
                        
                        // Update loan
                        new LoanDAO(conn).updateLoan(loan);
                        
                    // Check if the loan is less than 7 days until due date & the student has not been notified by e-mail about it
                    } else if (loan.getMailCount() == 0) {

                        String subject = "Komponent:" + loan.getBarcode() + "-StudieNr:" + loan.getStudentId();

                        String body = "Dette er en automatisk påmindelse til " + loan.getStudentId()
                                + ".\n\nDu har komponenten " + loan.getComponent().getComponentGroup().getName() + ". Du skal inden "
                                + (int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay) + 1)
                                + " dag(e) aflevere den eller henvende dig i Komponentshoppen på DTU Ballerup Campus og forlænge udlånet."
                                + " Afleveringsdatoen for komponenten er: " + loan.getDueDate()
                                + "\n\nMed venlig hilsen\nKomponentshoppen på DTU Ballerup Campus\n"
                                + "\n\n\n***Dette er en autogenereret e-mail. E-mails sendt til denne adresse vil ikke blive besvaret***";

                        SendEmail(subject, body, loan.getStudentId());
                        loan.setMailCount(1);
                        new LoanDAO(conn).updateLoan(loan);
                    }
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("No due loans found");
            ex.printStackTrace();
        }
    }

    private void SendEmail(String subject, String body, String user) {

        try {
            
            // Set reciever address
            //String to = user + "@student.dtu.dk";
            String to = "mailservicemis@gmail.com";           
            
            // Set sender address
            String from = "mailservicemis@gmail.com";
            
            // Set mail properties
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.from", from);
            mailProperties.put("mail.smtp.host", "smtp.gmail.com");     // Set SMTP to smtp.gmail,com
            mailProperties.put("mail.smtp.port", 465);                  // Use default SSL e-mail port (465)
            mailProperties.put("mail.smtp.auth", true);                 // Use Authetication
            
            // Setup SSL on port 465
            mailProperties.put("mail.smtp.socketFactory.port", 465);    
            mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProperties.put("mail.smtp.socketFactory.fallback", "false");
            mailProperties.put("mail.smtp.starttls.enable", "true");

            // Set SMTP-server user & password to the mail instance 
            Session mailSession = Session.getDefaultInstance(mailProperties, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(DatabaseConfig.MAILUSER, DatabaseConfig.PASSWORD);
                }

            });

            // Create e-mail message
            MimeMessage message = new MimeMessage(mailSession);
            
            // Set from address
            message.setFrom(new InternetAddress(from));

            // Set reeciever address
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            // Fill message - use UTF-8
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            message.setSubject(subject, "UTF-8");
            
            // Set date/time to message
            message.setSentDate(new java.util.Date());

            // Send e-mail
            Transport.send(message);
            
            System.out.println("Mail sent to " + user + " successfully");

        } catch (MessagingException ex) {
            System.out.println("Mail to " + user + " unsuccessful");
        }
    }
}
