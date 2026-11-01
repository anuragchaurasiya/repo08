package com.anurag.campusconnect.event;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Exposes CRUD endpoints for campus events. */
@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return findEvent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody Event request) {
        return eventRepository.save(new Event(
                request.getTitle(),
                request.getDescription(),
                request.getVenue(),
                request.getEventDate()
        ));
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @Valid @RequestBody Event request) {
        Event event = findEvent(id);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setEventDate(request.getEventDate());
        return eventRepository.save(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventRepository.delete(findEvent(id));
    }

    private Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }
}
