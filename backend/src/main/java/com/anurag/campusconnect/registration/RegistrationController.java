package com.anurag.campusconnect.registration;

import com.anurag.campusconnect.event.Event;
import com.anurag.campusconnect.event.EventRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Endpoints for registering attendees for campus events. */
@RestController
@RequestMapping("/api/events/{eventId}/registrations")
@CrossOrigin(origins = {"http://localhost:5173", "https://anuragchaurasiya.github.io"})
public class RegistrationController {
    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;

    public RegistrationController(EventRepository eventRepository,
                                   EventRegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse register(@PathVariable Long eventId,
                                         @Valid @RequestBody RegistrationRequest request) {
        Event event = findEvent(eventId);
        if (registrationRepository.existsByEvent_IdAndEmailIgnoreCase(eventId, request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This email is already registered for the event");
        }
        EventRegistration saved = registrationRepository.save(
                new EventRegistration(event, request.participantName(), request.email()));
        return RegistrationResponse.from(saved);
    }

    @GetMapping
    public List<RegistrationResponse> getRegistrations(@PathVariable Long eventId) {
        findEvent(eventId);
        return registrationRepository.findAllByEvent_IdOrderByRegisteredAtAsc(eventId)
                .stream().map(RegistrationResponse::from).toList();
    }

    @GetMapping("/count")
    public RegistrationCount getRegistrationCount(@PathVariable Long eventId) {
        findEvent(eventId);
        return new RegistrationCount(registrationRepository.countByEvent_Id(eventId));
    }

    @DeleteMapping("/{registrationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelRegistration(@PathVariable Long eventId, @PathVariable Long registrationId) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Registration not found"));
        if (!registration.getEventId().equals(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration not found");
        }
        registrationRepository.delete(registration);
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public record RegistrationCount(long count) {
    }
}
