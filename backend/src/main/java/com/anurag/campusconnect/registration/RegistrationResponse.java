package com.anurag.campusconnect.registration;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id,
        Long eventId,
        String participantName,
        LocalDateTime registeredAt
) {
    static RegistrationResponse from(EventRegistration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getEventId(),
                registration.getParticipantName(),
                registration.getRegisteredAt()
        );
    }
}
