package com.eventproject.controller;

import com.eventproject.dto.UpdateStatusEventDto;
import com.eventproject.model.Event;
import com.eventproject.service.EventService;
import com.eventproject.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired private EventService eventService;

    @GetMapping("/get-all-events")
    public ResponseEntity<List<Event>> getBlogs()
    {
        return ResponseEntity.ok().body(eventService.getAllEvents());
    }

    @PostMapping("/save-event")
    ResponseEntity<?> saveEvent(@Valid @RequestBody Event event) {
        if (event == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "You should provide information for saving event"),
                    HttpStatus.BAD_REQUEST);
        } else {
            eventService.saveEvent(event);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED,"Event Saved"),
                    HttpStatus.CREATED);
        }
    }

    @PutMapping("/update-event-status")
    ResponseEntity<?> updateEvent(@Valid @RequestBody UpdateStatusEventDto updateStatusEventDto,
                                  @RequestParam long idEvent) {
        Event event = eventService.getEventById(idEvent);
        if (event == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,"No event with the id provided"),
                    HttpStatus.BAD_REQUEST);
        } else {
            event.setEventValidated(updateStatusEventDto.getEventValidated());
            eventService.saveEvent(event);
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.OK,"Status updated successfully"),
                    HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete-event")
    ResponseEntity<?> deleteEvent(@RequestParam Long idEvent) {
        Event event = eventService.getEventById(idEvent);
        if (event == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Provide valid id for deleting Event"), HttpStatus.BAD_REQUEST);
        }
        else {
            eventService.deleteEvent(event);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Event deleted successfully"),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete-all-events")
    ResponseEntity<?> deleteAllEvents() {
        eventService.deleteAllEvents();
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"Events deleted successfully"),
                HttpStatus.OK);
    }
}
