package ge.appointmentservice.kafka;


import ge.appointmentservice.dto.kafka.AppointmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendAppointmentEventToKafka(AppointmentEvent event) {
        kafkaTemplate.send("appointment", event);
        log.info("Appointment with id {} has been sent to Kafka!", event.appointmentId());
    }

}
