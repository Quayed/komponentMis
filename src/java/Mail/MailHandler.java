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
                Thread.sleep(msPerDay / 4);
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
                if ((((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) < 7) && (loan.getDeliveryDate() == null || loan.getDeliveryDate().equals(""))) {
                    if ((((int) (((loan.getDueDateAsDate().getTime() - curDate.getTime()) / msPerDay))) <= 0) && loan.getMailCount() == 1) {
                        String subject = "Komponent:" + loan.getBarcode() + "-StudieNr:" + loan.getStudentId();

                        String body = "Dette er en automatisk påmindelse til " + loan.getStudentId()
                                + ".\n\nDu har overskredet afleveringsfristen for komponenten " + loan.getComponent().getComponentGroup().getName() + ". Du skal hurtigst muligt aflevere den i Komponentshoppen på DTU Ballerup Campus."
                                + " Afleveringsdatoen for komponenten var: " + loan.getDueDate()
                                + "\n\nMed venlig hilsen\nKomponentshoppen på DTU Ballerup Campus\n"
                                + "\n\n\n***Dette er en autogenereret e-mail. E-mails sendt til denne adresse vil ikke blive besvaret***";

                        SendEmail(subject, body, loan.getStudentId());
                        loan.setMailCount(1);
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
            String to = "mailservicemis@gmail.com";
            //String to = user + "@student.dtu.dk";
            String from = "mailservicemis@gmail.com";
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.from", from);
            mailProperties.put("mail.smtp.host", "smtp.gmail.com");
            mailProperties.put("mail.smtp.port", 465);
            mailProperties.put("mail.smtp.auth", true);
            mailProperties.put("mail.smtp.socketFactory.port", 465);
            mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProperties.put("mail.smtp.socketFactory.fallback", "false");
            mailProperties.put("mail.smtp.starttls.enable", "true");

            Session mailSession = Session.getDefaultInstance(mailProperties, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("mailservicemis@gmail.com", "passwordmis");
                }

            });

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            String[] emails = {to};
            InternetAddress dests[] = new InternetAddress[emails.length];
            for (int i = 0; i < emails.length; i++) {
                dests[i] = new InternetAddress(emails[i].trim().toLowerCase());
            }
            message.setRecipients(Message.RecipientType.TO, dests);
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new java.util.Date());

            Transport.send(message);
            System.out.println("Mail sent to " + user + " successfully");

        } catch (MessagingException ex) {
            System.out.println("Mail to " + user + " unsuccessful");
        }
    }
}
