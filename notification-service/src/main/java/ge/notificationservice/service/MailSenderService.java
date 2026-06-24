package ge.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;




    public void sendMail(String to, String subject, String body){
    var registrationMessage = new SimpleMailMessage();
     registrationMessage.setTo(to);
     registrationMessage.setSubject(subject);
     registrationMessage.setText(body);
     registrationMessage.setFrom(from);

     mailSender.send(registrationMessage);

     log.info("Mail sent to {}", to);
    }


}
