package com.eventproject.service;

import com.eventproject.model.Contact;
import com.eventproject.repository.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired private ContactRepo contactRepo;

    public Contact saveContact(Contact contact) {
        return contactRepo.save(contact);
    }

    public List<Contact> getAllcontact() {
        return contactRepo.findAll();
    }

    public void deleteContact(Contact contact) {
        contactRepo.delete(contact);
    }

    public void deleteAllContact() {
        contactRepo.deleteAll();
    }

    public void deleteContactById(Long id) {
        contactRepo.deleteContactByIdContact(id);
    }


}
