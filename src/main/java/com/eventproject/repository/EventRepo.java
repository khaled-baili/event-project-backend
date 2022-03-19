package com.eventproject.repository;

import com.eventproject.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
    Event findEventByIdEvent(long idEvent);
}
