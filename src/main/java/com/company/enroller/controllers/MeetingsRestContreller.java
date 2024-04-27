package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingsRestContreller {
    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings(@RequestParam(value = "sortBy", defaultValue = "") String sortBy,
                                             @RequestParam(value = "sortOrder", defaultValue = "") String sortOrder,
                                             @RequestParam(value = "key", defaultValue = "") String key) {
        Collection<Meeting> meetings = meetingService.getAll(sortBy, sortOrder, key);
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long meetingId) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if(foundMeeting != null){
            return new ResponseEntity<>("Unable to create. A meeting with id: "
                    + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long meetingId) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(foundMeeting);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long meetingId, @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
        }
        foundMeeting.setTitle(meeting.getTitle());
        foundMeeting.setDescription(meeting.getDescription());
        foundMeeting.setDate(meeting.getDate());
        meetingService.update(foundMeeting);
        return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
    }
}
