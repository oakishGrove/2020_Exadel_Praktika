package practice.guestRegistry.dao;

import org.bson.types.ObjectId;
import practice.guestRegistry.models.EventAttendee;

import java.util.List;
import java.util.Optional;

public interface EventsAttendeesDao {
// cia kurio reikia? :D
//    Optional<EventAttendee> findById (ObjectId id);
    List<EventAttendee> findById (ObjectId id);
    
    List<EventAttendee> findAll ();
    void add (EventAttendee eventAttendee);
    void update (ObjectId id, EventAttendee eventAttendee);
    void deleteById (ObjectId id);
    void deleteAll ();
}
