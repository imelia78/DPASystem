package ge.notificationservice.kafka;


import ge.notificationservice.dto.AppointmentEvent;
import ge.notificationservice.service.MailSenderService;
import ge.notificationservice.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableKafka
public class AppointmentKafkaConsumer {

    private final MailTemplateService mailTemplateService;
    private final MailSenderService mailSenderService;


    @KafkaListener(topics = "appointment")
    public void consumeAppointmentCreationEvent(AppointmentEvent event) {

        log.info("Received new appointment with id {}", event.appointmentId());

        var message = mailTemplateService.appointmentCreation(
                event.appointmentDateTime(),
                event.appointmentDuration(),
                event.clientFirstName(),
                event.clientLastName(),
                event.doctorFirstName(),
                event.doctorLastName()
        );

    }


}
