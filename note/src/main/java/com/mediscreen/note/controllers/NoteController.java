package com.mediscreen.note.controllers;

import com.mediscreen.note.models.Note;
import com.mediscreen.note.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class NoteController {

    private static final Logger      LOGGER = LoggerFactory.getLogger(NoteController.class);
    private final        NoteService noteService;

    public NoteController(NoteService noteService) {

        this.noteService = noteService;
    }

    @GetMapping("notes/all/patients/{patientId}")
    public ResponseEntity<List<Note>> browseByPatientId(@PathVariable Long patientId) {

        LOGGER.info("GET: /notes/patients/{}", patientId);

        return ResponseEntity.ok(this.noteService.getAllByPatientId(patientId));
    }


    @PostMapping(value = "notes", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Note> create(@Valid Note note) {

        LOGGER.info("GET: /notes/add");

        try {

            return new ResponseEntity<>(this.noteService.create(note), HttpStatus.CREATED);

        } catch (EntityExistsException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "notes/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Note> update(@PathVariable BigInteger id, @Valid Note note) {

        LOGGER.info("GET: /notes/{}", id);

        try {

            return ResponseEntity.ok(this.noteService.update(note));

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "patHistory/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> createWithCurl(Long patId, String notes) {

        if (patId == null || notes == null) {
            return new ResponseEntity<>("patId and notes is required", HttpStatus.BAD_REQUEST);
        }

        Note note = new Note();
        note.setPatientId(patId);
        note.setDate(LocalDateTime.now().withNano(0).toString());
        note.setNotes(notes);

        return this.create(note);
    }
}
