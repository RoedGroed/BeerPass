package Util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import java.util.Properties;

public class PrintAndMailUtility {

    public static void printTicket(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob(Printer.getDefaultPrinter());
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            job.printPage(node);
            job.endJob();
        }
    }

    public static void sendTicketByEmail(String to, String subject, String content) throws MessagingException {
        String from = "your-email@example.com";
        String password = "your-password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
