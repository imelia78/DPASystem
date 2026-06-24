package ge.appointmentservice.dto.kafka;

import java.util.UUID;

public record ClientRegisteredEvent(
        UUID clientId,
        String email,
        String firstName,
        String lastName
) {
}
