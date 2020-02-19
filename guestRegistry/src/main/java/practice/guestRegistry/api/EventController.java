package practice.guestRegistry.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.guestRegistry.models.Event;
import practice.guestRegistry.services.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    EventService service;

    @Autowired
    public EventController(EventService service) { this.service = service; }

    @GetMapping(path="{id}")
    public Optional<Event> getEvent(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @GetMapping
    public List<Event> getEvents() { return service.getAllEvents(); }

    @PostMapping
    public void addEvent(Event event) {
        service.addEvent(event); }

    @PutMapping(path="{id}")
    public void updateEvent(@PathVariable Long id, Event event) { service.updateEvent(id, event); }

    @DeleteMapping(path="{id}")
    public void deleteEvent(@PathVariable Long id) { service.deleteEventById(id); }
}