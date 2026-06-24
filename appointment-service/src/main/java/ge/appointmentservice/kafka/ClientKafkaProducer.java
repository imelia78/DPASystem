package ge.appointmentservice.kafka;


import ge.appointmentservice.dto.kafka.AppointmentEvent;
import ge.appointmentservice.dto.kafka.ClientRegisteredEvent;
import ge.appointmentservice.dto.kafka.DoctorRegisteredEvent;
import ge.appointmentservice.model.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendClientRegisteredEventToKafka(ClientRegisteredEvent event) {
        kafkaTemplate.send("client_registration", event);
        log.info("Client with id {} has been sent to Kafka!", event.clientId());
    }


}
