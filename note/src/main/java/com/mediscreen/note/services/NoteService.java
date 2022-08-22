package com.mediscreen.note.services;

import com.mediscreen.note.models.Note;
import com.mediscreen.note.repositories.NoteRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {

        this.noteRepository = noteRepository;
    }

    public List<Note> getAllByPatientId(Long patientId) {

        return this.noteRepository.findAllByPatientId(patientId);
    }

    public Note create(Note note) {

        if (note.getId() != null) {

            throw new EntityExistsException("Note already exist or id isn't null");
        }

        return this.noteRepository.insert(note);
    }

    public Note update(Note note) {

        if (note.getId() == null) {

            throw new IllegalArgumentException("Note have must id");
        }

        if (!this.noteRepository.existsById(note.getId())) {

            throw new NoSuchElementException("Note not found");
        }

        return this.noteRepository.save(note);
    }
}
