package application;

import java.util.Date;
import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SendEmailOffice365 {

	private String AccountName;
	private String Password;
    	private String from;
	private String to;
    	private String subject;
	private String messageContent;


	public SendEmailOffice365(String AccountName,String Password,String to,
			String subject, String messageContent){
		this.AccountName = AccountName;
		this.from = AccountName;
		this.Password = Password;
		this.to = to;
		this.messageContent = messageContent;
		this.subject = subject;
	}

    //private static final Logger LOGGER = Logger.getAnonymousLogger();

    private static final String SMTP_Server = "smtp.kfupm.edu.sa";
    private static final int SMTP_Port = 587;

    public boolean sendEmail() {
        final Session session = Session.getInstance(this.getEmailProperties(), new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AccountName, Password);
            }

        });

        try {
            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setText(messageContent);
            message.setSentDate(new Date());
            Transport.send(message);
        } 
        catch (final MessagingException ex) {
        	Alert emptyEmailAlert = new Alert(AlertType.ERROR);
			emptyEmailAlert.setContentText("Authentication unsuccessful");
			emptyEmailAlert.showAndWait();
            //LOGGER.log(Level.WARNING, "Error Sending Message: " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    //BONUS feature: the ports and smtp servers are returned according to email address. Therefore, user is not restricted only to kfupm email. 
    //Now he can use other emails like gmail and yahoo.
    public Properties getEmailProperties() {
        final Properties config = new Properties();
        if(AccountName.contains("@kfupm.edu.sa")) {
        	config.put("mail.smtp.auth", "true");
        	config.put("mail.smtp.starttls.enable", "true");
        	config.put("mail.smtp.host", SMTP_Server);
        	config.put("mail.smtp.port", SMTP_Port);
        	return config;
        }
        else if(AccountName.contains("@gmail.com")) {
        	config.put("mail.smtp.auth", "true");
        	config.put("mail.smtp.starttls.enable", "true");
        	config.put("mail.smtp.host", "smtp.gmail.com");
        	config.put("mail.smtp.port", "587");
        	return config;
        } 
        else if(AccountName.contains("@yahoo.com")) {
        	config.put("mail.smtp.auth", "true");
        	config.put("mail.smtp.starttls.enable", "true");
        	config.put("mail.smtp.host", "smtp.mail.yahoo.com");
        	config.put("mail.smtp.port", "465");
        	return config;
        }
        return config;
    }
}