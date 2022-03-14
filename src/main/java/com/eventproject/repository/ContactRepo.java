package com.eventproject.repository;

import com.eventproject.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ContactRepo extends JpaRepository <Contact, Long> {
    @Transactional
    void deleteContactByIdContact(Long id);
}
