package com.anurag.campusconnect.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findAllByEvent_IdOrderByRegisteredAtAsc(Long eventId);
    boolean existsByEvent_IdAndEmailIgnoreCase(Long eventId, String email);
    long countByEvent_Id(Long eventId);
}
