package com.anurag.campusconnect.registration;

import com.anurag.campusconnect.event.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** A student's registration for one campus event. */
@Entity
@Table(name = "event_registrations",
        uniqueConstraints = @UniqueConstraint(name = "uk_event_registration_email",
                columnNames = {"event_id", "email"}))
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String participantName;

    @NotBlank
    @Email
    @Size(max = 254)
    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    protected EventRegistration() {
        // Required by JPA.
    }

    public EventRegistration(Event event, String participantName, String email) {
        this.event = event;
        this.participantName = participantName;
        this.email = email.trim().toLowerCase();
        this.registeredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getEventId() { return event.getId(); }
    public String getParticipantName() { return participantName; }
    public String getEmail() { return email; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
