package com.mits.edelweis.export;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.filenet.apiimpl.util.Crypto;
import com.mits.edelweiss.VWUtils.PropertiesUtil;


public class Mailcomponenet {

	
    static Properties messageProperties = null;

    public void sendEmailTemplate(final String from ,String to, String cc, final String password){  

    	Logger log = Logger.getLogger(Mailcomponenet.class);

    	log.info("Entered into sendEmailTemplate method in SendMail Class");
//	log.info("Entered into sendEmailTemplate method in SendMail Class");
	Session mailSession = null;
	Message message = null;
	@SuppressWarnings("unused")
	String isMailSent="";
	Writer out = null;
	Properties props1 = PropertiesUtil.getInstance();

	Properties props = null;

	try {
		
		//Properties props1 = PropertiesUtil.getInstance();

	    props = System.getProperties();

	    props.setProperty(props1.getProperty("mailsmtpport"), props1.getProperty("Port"));
	    props.setProperty(props1.getProperty("mailsmtphost"), props1.getProperty("smtpoffice365com"));
	    props.setProperty(props1.getProperty("mailsmtpauth"), props1.getProperty("true"));
	    props.setProperty(props1.getProperty("mailsmtpssltrust"),props1.getProperty("smtpoffice365com"));
	    props.setProperty(props1.getProperty("mailsmtpstarttlsenable"), props1.getProperty("true"));

	    mailSession = Session.getInstance(props, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(from,password);
		}
	    });
	   // out = new StringWriter();
	    message = new MimeMessage(mailSession);
	   
	   message.setFrom(new InternetAddress(from));
	    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	   
		//	message.setText("PFA");
		    message.setSubject(props1.getProperty("Subject")); 
		          
		      
                 
		   
		BodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setText(props1.getProperty("Text"));	
		
		Multipart multipart = new MimeMultipart();

		multipart.addBodyPart(messageBodyPart);
		
		messageBodyPart = new MimeBodyPart();
			File file =new File("./Exportdata.xls");
			//String file = "E:/New folder/Exportdata.xls";
			//String fileName = "Export Data.xls";
			
			
			FileDataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			
			messageBodyPart.setFileName(file.getName());
	
			multipart.addBodyPart(messageBodyPart);	
			
			message.setContent(multipart);
		
			//message.setText("This is a sample data from report");
		

			log.info("Sending");
			
			 // message.setText("This is a sample data from report"); 
			  
			Transport.send(message);

			log.info("Done");

	    isMailSent ="Success";
	    log.info("message sent....");  
	}catch (MessagingException ex) {
	    isMailSent ="false";
	    ex.printStackTrace();
	    log.info("MessagingException ex:::"+ex.getMessage());
	}
	finally
	{
	    mailSession = null;
	    message = null;
	 
	    if(out!=null)
	    {
		try {
		    out.close();
		} catch (IOException e) {

		    e.printStackTrace();
		}
		out = null;
	    }
	    props = null;
	}
	log.info("Ended into sendEmailTemplate method in SendMail Class");
    }
   /* public String startDate() throws ParseException{
	Calendar cal = Calendar.getInstance();
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	cal.add(Calendar.DATE, -1);
	String getDayBeforYesterdayDateString = dateFormat.format(cal.getTime());
	return getDayBeforYesterdayDateString;
    }
    */
    public static void main(String[] args) throws GeneralSecurityException, IOException {
    	Properties props1 = PropertiesUtil.getInstance();

    	FinalReport Freport = new FinalReport();
    	String MailPwd = props1.getProperty("UserMailPassword");
		System.out.println("Encry :::::::pw"+MailPwd);
		String decryptPW = CryptoUtil.decryptPhrase(MailPwd);
		
		System.out.println("decryptPW::::"+decryptPW);
		
    	Freport.finalreport();
       Mailcomponenet sendMail = new Mailcomponenet();
	sendMail.sendEmailTemplate(props1.getProperty("From"),props1.getProperty("To"),props1.getProperty("Cc"),decryptPW);
    }
}
	