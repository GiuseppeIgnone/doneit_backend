package com.sini.doneit.controller;

import com.sini.doneit.jwt.JwtTokenUtil;
import com.sini.doneit.model.Event;
import com.sini.doneit.model.EventPartecipation;
import com.sini.doneit.model.ResponseMessage;
import com.sini.doneit.model.User;
import com.sini.doneit.repository.EventJpaRepository;
import com.sini.doneit.repository.EventPartecipationJpaRepository;
import com.sini.doneit.repository.UserJpaRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.sini.doneit.model.MessageCode.*;

@RestController
@CrossOrigin("*")
public class EventController {

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    EventPartecipationJpaRepository eventPartecipationJpaRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping(path = "create-event")
    public ResponseEntity<ResponseMessage> createEvent(@RequestBody Event event, @RequestHeader HttpHeaders headers) {
        User owner = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(headers));
        event.setUser(owner);
        eventJpaRepository.save(event);
        return new ResponseEntity<>(new ResponseMessage("Evento creato correttamente", EVENT_CREATED),
                HttpStatus.OK);
    }

    @GetMapping(path = "active-event-list")
    public List<Event> getActiveEvents(@RequestHeader HttpHeaders httpHeaders) {
        User owner = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        List<Event> eventList = eventJpaRepository.findEventList();
        System.out.println(eventList);
        return eventList;
    }

    @GetMapping(path = "event-list/users/{username}")
    public List<Event> getEventsByUsername(@PathVariable String username){
        User user = this.userJpaRepository.findByUsername(username);
        return this.eventJpaRepository.findByUser(user);
    }

    @GetMapping(path = "/event/my-events")
    public List<Event> getMyEvents(@RequestHeader HttpHeaders httpHeaders) {
        User user = this.userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        return this.eventJpaRepository.findByUser(user);
    }

    @GetMapping(path = "/event/get-events/{username}")
    public List<Event> getEventListByUsername(@PathVariable String username) {
        User user = this.userJpaRepository.findByUsername(username);
        return this.eventJpaRepository.findByUser(user);
    }

    @PostMapping("/event/join-event")
    public ResponseEntity<ResponseMessage> joinEvent(@RequestBody Event e, @RequestHeader HttpHeaders headers) {
        System.out.println(e);
        User user = this.userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(headers));
        Optional<Event> currentEvent = eventJpaRepository.findById(e.getId());
        if (!e.getUser().getUsername().equals(user.getUsername()) && currentEvent.isPresent()) {
            e = currentEvent.get();
            EventPartecipation partecipation = new EventPartecipation();
            partecipation.setUser(user);
            partecipation.setEvent(e);
            eventPartecipationJpaRepository.save(partecipation);
            return new ResponseEntity<>(new ResponseMessage("Proposta all'evento con successo"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseMessage("Proposta non andata a buon fine"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/event/get-event/{id}")
    public Event getEvent(@PathVariable Long id) {
        Optional<Event> optEvent = this.eventJpaRepository.findById(id);
        if (optEvent.isPresent()) {
            return optEvent.get();
        }
        return null;
    }

    @GetMapping("/event/get-my-joined-events")
    public List<Event> getMyJoinedEvents(@RequestHeader HttpHeaders httpHeaders) {
        User user = userJpaRepository.findByUsername(jwtTokenUtil.getUsernameFromHeader(httpHeaders));
        List<Event> joinedEvents = this.eventPartecipationJpaRepository.getMyJoinedEvent(user);
        return joinedEvents;
    }
}
