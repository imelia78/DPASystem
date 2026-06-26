package ge.notificationservice.service;


import ge.notificationservice.dto.MailMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailTemplateService {

    private final MailSenderService mailSenderService;


    public MailMessage clientRegistration(String firstName, String lastName) {
        return new MailMessage(
                "Welcome to Health Bridge Application!",
                """
                        Dear %s %s
                        
                        Your account has been successfully created and is ready to use.
                        You can now browse available doctors and schedule appointments online.
                        
                        
                        Best regards,
                        
                        Health Bridge Team
                        """.formatted(firstName, lastName)
        );
    }

    public MailMessage doctorRegistration(String firstName, String lastName) {
        return new MailMessage(
                "Welcome to Health Bridge Application!",
                """
                        Dear %s %s
                        
                        Your request has been successfully received by our team.
                        We will consider it and update your status in our application.
                        
                        Best regards,
                        
                        Health Bridge Team
                        """.formatted(firstName, lastName)
        );
    }

    public MailMessage appointmentCreation(LocalDateTime appointmentDateTime,
                                           Integer appointmentDuration,
                                           String clientFirstName,
                                           String clientLastName,
                                           String doctorFirstName,
                                           String doctorLastName) {


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' HH:mm", Locale.ENGLISH);

        String formattedDateTime = appointmentDateTime.format(formatter);

        return new MailMessage(
                "Scheduled Appointment",
                """
                        Dear %s %s
                        
                        Your appointment has been successfully scheduled!
                        
                         Doctor: %s %s
                         Date & Time: %s
                         Duration: %d minutes
                        
                        Please arrive 5–10 minutes before your appointment.
                        
                        Best regards,
                        
                        Health Bridge Team
                        """.formatted(
                        clientFirstName,
                        clientLastName,
                        doctorFirstName,
                        doctorLastName,
                        formattedDateTime,
                        appointmentDuration)

        );
    }
}


