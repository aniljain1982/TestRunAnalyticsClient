package com.automation.emailer;

import java.io.File;
import java.util.Base64;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.automation.helper.GeneralHelper;
import com.automation.helper.PropertyHelper;

public class Emailer {

	public static Boolean sslFlag;

	public static String decodePassword(String encodedPwd) {
		byte[] decoded = Base64.getDecoder().decode(encodedPwd);
		return new String(decoded);
	}

	public static String encodePassword(String pwd) {
		return Base64.getEncoder().encodeToString(pwd.getBytes());
	}

	public void sendEmail(GlobalPOJO global) throws Exception {
		PropertyHelper propertyHelper=new PropertyHelper("emailerConfig.properties");
		final String user = propertyHelper.getPropertyValue("username");
		
		//final String password = Emailer.decodePassword("");
		final String password=propertyHelper.getPropertyValue("password");
		
		//String host = "smtp.office365.com";
		String host="smtp.gmail.com";
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
//		if (sslFlag) {
//			props.put("mail.smtp.ssl.trust", "*");
//		}
		props.put("mail.store.protocol", "SMTP");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom((Address) new InternetAddress("aniljain1982@gmail.com"));
			InternetAddress[] recipientAddress = new InternetAddress[(global.getRecipients().split(",")).length];
			int counter = 0;
			for (String toRcipient : global.getRecipients().split(",")) {
				recipientAddress[counter] = new InternetAddress(toRcipient.trim());
				counter++;
			}
			message.setRecipients(Message.RecipientType.TO, (Address[]) recipientAddress);
			InternetAddress[] ccRecipientAddress = new InternetAddress[(global.getCcRecipients().split(",")).length];
			counter = 0;
			for (String Rcipient : global.getCcRecipients().split(",")) {
				ccRecipientAddress[counter] = new InternetAddress(Rcipient.trim());
				counter++;
			}

			message.setRecipients(Message.RecipientType.CC, (Address[]) ccRecipientAddress);
			message.setSubject(global.getSubject());
			MimeMultipart mimeMultipart = new MimeMultipart();

			if (!global.getPieChart().isEmpty()) {
				MimeBodyPart pchart = new MimeBodyPart();
				DataSource fds = new FileDataSource(
						System.getProperty("user.dir") + File.separator + global.getPieChart());
				pchart.setDataHandler(new DataHandler(fds));
				pchart.setHeader("Content-ID", "piechart");
				mimeMultipart.addBodyPart(pchart);
			}
			
			MimeBodyPart attachment = new MimeBodyPart();
			String file = global.getProjectName().toUpperCase() + ".html";
			DataSource source = new FileDataSource(global.getFileAttachment());
			attachment.setDataHandler(new DataHandler(source));
			attachment.setFileName(file);
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(global.getEmailBody(), "text/html");
			
			mimeMultipart.addBodyPart(attachment);
			mimeMultipart.addBodyPart(htmlPart);
			message.setContent(mimeMultipart);
			Transport.send(message);
		} catch (Exception e) {
			throw new Exception("Issue in sending email --> " + new GeneralHelper().convertExceptionToString(e));
		}

	}
}
