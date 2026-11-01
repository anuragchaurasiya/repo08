package com.anurag.campusconnect.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Basic domain tests for event data. */
class EventTest {
    @Test
    void constructorStoresEventDetails() {
        LocalDateTime date = LocalDateTime.of(2027, 2, 15, 10, 0);
        Event event = new Event("Tech Fest", "Annual technology festival", "Main Auditorium", date);

        assertEquals("Tech Fest", event.getTitle());
        assertEquals("Annual technology festival", event.getDescription());
        assertEquals("Main Auditorium", event.getVenue());
        assertEquals(date, event.getEventDate());
    }
}
