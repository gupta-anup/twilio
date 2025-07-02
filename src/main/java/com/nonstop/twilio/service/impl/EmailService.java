package com.nonstop.twilio.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.enums.MessageChannel;
import com.nonstop.twilio.service.MessageService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService implements MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;
    
    @Value("${sendgrid.from-email}")
    private String fromEmail;
    
    @Value("${sendgrid.from-name:Twilio Messaging Service}")
    private String fromName;
    
    @Override
    public boolean supports(MessageChannel channel) {
        return MessageChannel.EMAIL.equals(channel);
    }
    
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            logger.info("Sending email via SendGrid to: {}", request.getRecipient());
            
            // validate email
            if (!isValidEmail(request.getRecipient())) {
                logger.error("Invalid email address: {}", request.getRecipient());
                return new MessageResponse(
                    false,
                    "Invalid email address format: " + request.getRecipient(),
                    null,
                    MessageChannel.EMAIL,
                    request.getRecipient()
                );
            }
            
            SendGrid sg = new SendGrid(sendGridApiKey);
            
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(request.getRecipient());
            
            // use provided subject or default
            String subject = StringUtils.hasText(request.getSubject()) 
                ? request.getSubject() 
                : "Message from Twilio Messaging Service";
            
            // create content (HTML or plain text)
            Content content;
            if (StringUtils.hasText(request.getHtmlContent())) {
                content = new Content("text/html", request.getHtmlContent());
            } else {
                content = new Content("text/plain", request.getMessage());
            }
            
            // create mail
            Mail mail = new Mail(from, subject, to, content);
            
            // create request
            Request sgRequest = new Request();
            sgRequest.setMethod(Method.POST);
            sgRequest.setEndpoint("mail/send");
            sgRequest.setBody(mail.build());
            
            // send email
            Response response = sg.api(sgRequest);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("Email sent successfully via SendGrid to: {} with status code: {}", 
                    request.getRecipient(), response.getStatusCode());
                
                // generate message ID for tracking
                String messageId = "SG_" + System.currentTimeMillis() + "_" + 
                    Math.abs(request.getRecipient().hashCode());
                
                return new MessageResponse(
                    true,
                    "Email sent successfully via SendGrid",
                    messageId,
                    MessageChannel.EMAIL,
                    request.getRecipient()
                );
            } else {
                logger.error("Failed to send email via SendGrid to: {}. Status: {}, Body: {}", 
                    request.getRecipient(), response.getStatusCode(), response.getBody());
                
                return new MessageResponse(
                    false,
                    "Failed to send email via SendGrid: Status " + response.getStatusCode(),
                    null,
                    MessageChannel.EMAIL,
                    request.getRecipient()
                );
            }
            
        } catch (IOException e) {
            logger.error("IO error sending email via SendGrid to: {}", request.getRecipient(), e);
            return new MessageResponse(
                false,
                "Failed to send email: " + e.getMessage(),
                null,
                MessageChannel.EMAIL,
                request.getRecipient()
            );
        } catch (Exception e) {
            logger.error("Unexpected error sending email via SendGrid to: {}", request.getRecipient(), e);
            return new MessageResponse(
                false,
                "Failed to send email: " + e.getMessage(),
                null,
                MessageChannel.EMAIL,
                request.getRecipient()
            );
        }
    }
    
    // email validation
    private boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && 
               email.contains("@") && 
               email.contains(".") && 
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
