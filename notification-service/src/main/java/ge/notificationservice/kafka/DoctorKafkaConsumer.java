package ge.notificationservice.kafka;


import ge.notificationservice.dto.DoctorRegisteredEvent;
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
public class DoctorKafkaConsumer {

    private final MailSenderService mailSenderService;
    private final MailTemplateService mailTemplateService;


    @KafkaListener(topics = "doctor_registration")
    public void consumeDoctorRegistrationRequest(DoctorRegisteredEvent event) {

        log.info("Registration request for doctor with id {} has been successfully received!", event.doctorId());

        var message = mailTemplateService.doctorRegistration(
                event.firstName(),
                event.lastName()
        );

        mailSenderService.sendMail(
                event.email(),
                message.subject(),
                message.body()

        );

    }

}
