package com.example.projectx.mail;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.example.projectx.mail.Mail;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(Mail mail) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());       
       

        emailSender.send(message);

    }
    
    public void sendSimpleMessage(Mail mail, File file) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());
        
        helper.addAttachment(file.getName(), file);      

        emailSender.send(message);

    }
    
    public void sendHtmlMessage(Mail mail, File file) throws MessagingException {

    	
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(mail.getSubject());
        
        helper.setText(mail.getContent(),true);
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());
        
        helper.addAttachment(file.getName(), file); 
        

        emailSender.send(message);

    }
    public void sendHtmlMessage(Mail mail, List<File> files) throws MessagingException {

    	
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(mail.getSubject());
        
        helper.setText(mail.getContent(),true);
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());
        
        for(File file:files)
        {
        	helper.addAttachment(file.getName(), file); 
        }        
        

        emailSender.send(message);

    }
    public void sendHtmlMessage(Mail mail) throws MessagingException {

    	
        MimeMessage message = emailSender.createMimeMessage();
        
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        
        helper.setSubject(mail.getSubject());
        
        helper.setText(mail.getContent(),true);
        helper.setTo(mail.getTo());
        helper.setFrom(mail.getTo());        
            

        emailSender.send(message);

    }
    
    

}
