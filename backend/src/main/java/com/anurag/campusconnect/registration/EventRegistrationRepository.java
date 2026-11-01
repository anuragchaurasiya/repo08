package com.anurag.campusconnect.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findAllByEventIdOrderByRegisteredAtAsc(Long eventId);
    boolean existsByEventIdAndEmailIgnoreCase(Long eventId, String email);
    long countByEventId(Long eventId);
}
