package com.mediscreen.note.controllers;

import com.mediscreen.note.models.Note;
import com.mediscreen.note.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final Logger      LOGGER = LoggerFactory.getLogger(NoteController.class);
    private final        NoteService noteService;

    public NoteController(NoteService noteService) {

        this.noteService = noteService;
    }

    @GetMapping("/all/patients/{patientId}")
    public ResponseEntity<List<Note>> browseByPatientId(@PathVariable Long patientId) {

        LOGGER.info("GET: /notes/patients/{}", patientId);

        return ResponseEntity.ok(this.noteService.getAllByPatientId(patientId));
    }


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Note> create(@Valid Note note) {

        LOGGER.info("GET: /notes/add");

        try {

            return ResponseEntity.ok(this.noteService.create(note));

        } catch (EntityExistsException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
}
