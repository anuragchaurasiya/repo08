package com.anurag.campusconnect.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** JPA entity representing a campus event. */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String title;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @NotBlank
    @Size(max = 160)
    @Column(nullable = false, length = 160)
    private String venue;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    protected Event() {
        // Required by JPA.
    }

    public Event(String title, String description, String venue, LocalDateTime eventDate) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.eventDate = eventDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVenue() { return venue; }
    public LocalDateTime getEventDate() { return eventDate; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
}
