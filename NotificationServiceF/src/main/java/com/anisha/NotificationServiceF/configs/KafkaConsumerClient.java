package com.anisha.NotificationServiceF.configs;


import com.anisha.NotificationServiceF.dtos.SendEmailDto;
import com.anisha.NotificationServiceF.services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Component
public class KafkaConsumerClient  {
    private EmailService emailService;
    private ObjectMapper objectMapper;

    public KafkaConsumerClient (  ObjectMapper objectMapper , EmailService emailService ){
        this.objectMapper= objectMapper;
        this.emailService= emailService;
    }


    @KafkaListener( topics =  "userSignUp", groupId = "notifService")
    public void handleSendEmailUserSignUp( String message ){
        try {
            SendEmailDto messageDto = objectMapper.readValue(message, SendEmailDto.class);
            String smtpHostServer = "smtp.gmail.com";
            String emailID = "anishadhillon08@gmail.com ";

            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHostServer);
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication( messageDto.getFrom(), "${password}");
                }
            };

            Session session = Session.getInstance(props, auth);

            emailService.sendEmail(session, messageDto.getTo(), messageDto.getSubject(), messageDto.getBody());

        }
        catch( JsonProcessingException e ){
            System.out.println("Failed to parse message: " + message);
            e.printStackTrace();
        }

    }
    @KafkaListener( topics =  "orderStatus", groupId = "notifService")
    public void handleSendEmailOrderStatus( String message ) {
        try {
            SendEmailDto messageDto = objectMapper.readValue(message, SendEmailDto.class);
            String smtpHostServer = "smtp.gmail.com";

            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHostServer);
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(messageDto.getFrom(), "lrpmoitqoatxxsag");
                }
            };

            Session session = Session.getInstance(props, auth);

            emailService.sendEmail(session, messageDto.getTo(), messageDto.getSubject(), messageDto.getBody());

        } catch (JsonProcessingException e) {
            System.out.println("Failed to parse message: " + message);
            e.printStackTrace();
        }

    }
}
