package ge.notificationservice.kafka;

import ge.notificationservice.dto.ClientRegisteredEvent;
import ge.notificationservice.service.MailSenderService;
import ge.notificationservice.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class ClientKafkaConsumer {

    private final MailTemplateService mailTemplateService;
    private final MailSenderService mailSenderService;


    @KafkaListener(topics = "client_registration")
    public void consumeClientRegisteredEvent(ClientRegisteredEvent event) {

        log.info("Received client registration for client: {} !", event.clientId());

        var message = mailTemplateService.clientRegistration(
                event.firstName(), event.lastName()
        );

        mailSenderService.sendMail(
                event.email(),
                message.subject(),
                message.body()
        );

    }

}
