/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mail;

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

    int msPerDay = 86400 * 1000;

    public MailHandler() {
    }

    @Override
    public void run() {
        while (true) {
            try {               
                SendEmail("TestEmne", "TestBody", "mailservicemis@gmail.com");
                Thread.sleep(msPerDay);
            } catch (InterruptedException ex) {
                System.out.println("MailHandler stopped");
            }
        }
    }

    private void SendEmail(String subject, String body, String address) {

        // Recipient's email ID needs to be mentioned.
        String to = address;

        // Sender's email ID needs to be mentioned
        String from = "mailservicemis@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("smtp-relay.gmail.com", host);

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
            System.out.println("Sent message successfully");
        } catch (MessagingException mex) {
            System.out.println("Sent message unsuccessfully " + mex.getMessage());
        }
    }
}
