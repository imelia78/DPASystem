package ge.appointmentservice.kafka;


import ge.appointmentservice.dto.kafka.DoctorRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendDoctorRegisteredEventToKafka(DoctorRegisteredEvent event){
        kafkaTemplate.send("doctor_registration", event);
        log.info("Doctor with id {} has been sent to Kafka!", event.doctorId());
    }

}
