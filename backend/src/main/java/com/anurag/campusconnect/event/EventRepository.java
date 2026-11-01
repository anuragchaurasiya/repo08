package com.anurag.campusconnect.event;

import org.springframework.data.jpa.repository.JpaRepository;

/** Provides database operations for campus events. */
public interface EventRepository extends JpaRepository<Event, Long> {
}
