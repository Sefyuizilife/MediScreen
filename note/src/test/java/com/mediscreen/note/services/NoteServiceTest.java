package com.mediscreen.note.services;


import com.mediscreen.note.models.Note;
import com.mediscreen.note.repositories.NoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NoteService.class})
public class NoteServiceTest {

    private final NoteService    noteService;
    private final List<Note>     notes = new ArrayList<>();
    @MockBean
    private       NoteRepository noteRepository;

    public NoteServiceTest(@Autowired NoteService noteService) {

        this.noteService = noteService;
    }

    @BeforeEach
    public void setUpPerTest() {

        for (int i = 0 ; i <= 2 ; i++) {
            Note note = new Note();
            note.setId(new BigInteger(String.valueOf(i) + 1));
            note.setPatientId(1L);
            note.setDate(LocalDateTime.now().withSecond(2).withNano(0).toString());
            note.setNotes(String.format("My note %s", i));

            this.notes.add(note);
        }
    }

    @Test
    public void getAllByPatientId_shouldReturnAllNotes() {

        long patientId = 1L;

        when(this.noteRepository.findAllByPatientId(patientId)).thenReturn(this.notes);

        List<Note> results = this.noteService.getAllByPatientId(patientId);

        Assertions.assertThat(results).isSameAs(this.notes);
    }

    @Test
    public void update_shouldReturnUpdatedNote_forExistsNote() {

        Note note = this.notes.get(0);
        when(this.noteRepository.existsById(note.getId())).thenReturn(true);
        when(this.noteRepository.save(note)).thenReturn(note);

        Assertions.assertThat(this.noteService.update(note)).isSameAs(note);
    }

    @Test
    public void update_shouldThrowException_forNotExistsNote() {

        Note note = this.notes.get(0);
        when(this.noteRepository.existsById(note.getId())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
            this.noteService.update(note);
        }).isInstanceOf(NoSuchElementException.class).hasMessageContaining("Note not found");
    }

    @Test
    public void update_shouldThrowException_forNoteWithoutId() {

        Note note = this.notes.get(0);
        note.setId(null);

        Assertions.assertThatThrownBy(() -> {
            this.noteService.update(note);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Note have must id");
    }

    @Test
    public void create_shouldReturnCreatedNote_forNewNote() {

        Note newNote = this.notes.get(0);
        newNote.setId(null);

        when(this.noteRepository.insert(newNote)).thenReturn(this.notes.get(0));

        Assertions.assertThat(this.noteService.create(newNote)).isSameAs(this.notes.get(0));
    }

    @Test
    public void create_shouldThrowException_forNoteExisting() {

        Note newNote = this.notes.get(0);

        when(this.noteRepository.existsById(newNote.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            this.noteService.create(newNote);
        }).isInstanceOf(EntityExistsException.class).hasMessageContaining("Note already exist or id isn't null");
    }
}