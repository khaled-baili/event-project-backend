package com.eventproject.controller;

import com.eventproject.model.Contact;
import com.eventproject.service.ContactService;
import com.eventproject.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    @Autowired private ContactService contactService;

    @GetMapping("/get-contacts")
    ResponseEntity<?> getAllContacts() {
        return new ResponseEntity<>(contactService.getAllcontact(), HttpStatus.OK);
    }

    @PostMapping("/save-contact")
    ResponseEntity<?> savaContact(@RequestBody Contact contact) {
        if (contact == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,"no information provided")
                , HttpStatus.BAD_REQUEST);
        else {
            contactService.saveContact(contact);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED,
                    "Check your email we will back for you soon"), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("delete-contact")
    ResponseEntity<?> deleteContact(@RequestParam Long idcontact) {
        if (idcontact == null)
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Provide id for deleting contact message"), HttpStatus.BAD_REQUEST);
        else {
            contactService.deleteContactById(idcontact);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Contact message deleted successfully"),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("delete-all-contacts")
    ResponseEntity<?> deleteAllContact() {
        contactService.deleteAllContact();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Contacts messages deleted successfully"),
                    HttpStatus.OK);
    }
}
