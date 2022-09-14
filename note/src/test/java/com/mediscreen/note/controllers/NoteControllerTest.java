package com.mediscreen.note.controllers;

import com.mediscreen.note.models.Note;
import com.mediscreen.note.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    private final MockMvc    mockMvc;
    private final List<Note> notes = new ArrayList<>();

    @MockBean
    private NoteService noteService;

    public NoteControllerTest(@Autowired MockMvc mockMvc) {

        this.mockMvc = mockMvc;
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

    //
    @Test
    public void browse_shouldAllNotesByPatient_forExistingPatientId() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.getAllByPatientId(note.getPatientId())).thenReturn(this.notes);
        String resultExpected = this.notesToJson(this.notes);

        this.mockMvc.perform(get("/notes/all/patients/{patientId}", note.getPatientId()))
                    .andDo(print())
                    .andExpect(content().json(resultExpected))
                    .andExpect(status().isOk());
    }


    @Test
    public void browse_should404_forNotExistingPatientId() throws Exception {

        when(this.noteService.getAllByPatientId(anyLong())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/notes/all/patients/{patientId}", 99l))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(this.notesToJson(new ArrayList<>())));
    }

    @Test
    public void create_should400_forBadRequest() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.create(any(Note.class))).thenThrow(
                new EntityExistsException("Patient already exist or id isn't null"));

        this.mockMvc.perform(post("/notes").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                           .param("patientId", note.getPatientId().toString())
                                           .param("date", note.getDate().plusDays(1).format(Note.DATE_TIME_FORMATTER))
                                           .param("notes", note.getNotes()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void update_should400_forBadRequest() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.update(any())).thenThrow(new NoSuchElementException("Patient not found"));

        this.mockMvc.perform(put("/notes/{id}", note.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                             .param("patientId", note.getPatientId().toString())
                                                             .param("notes", note.getNotes())
                                                             .param("date", note.getDate().toString()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void update_should404_PatientHaventId() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.update(any())).thenThrow(new IllegalArgumentException("Patient not found"));

        this.mockMvc.perform(put("/notes/{id}", note.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                             .param("patientId", note.getPatientId().toString())
                                                             .param("notes", note.getNotes())
                                                             .param("date", note.getDate().toString()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void create_shouldRegisterNote_forCorrectData() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.create(any(Note.class))).thenReturn(note);

        this.mockMvc.perform(post("/notes/").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                            .param("patientId", note.getPatientId().toString())
                                            .param("notes", note.getNotes())
                                            .param("date", note.getDate().toString()))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json(this.noteToJson(note)));
    }

    @Test
    public void update_shouldNoteUpdated_ForExistingNote() throws Exception {

        Note note = this.notes.get(0);

        when(this.noteService.update(any(Note.class))).thenReturn(note);

        this.mockMvc.perform(put("/notes/{id}", note.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                             .param("patientId", note.getPatientId().toString())
                                                             .param("notes", note.getNotes())
                                                             .param("date", note.getDate().toString()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(this.noteToJson(note)));

    }

    private String noteToJson(Note note) {

        StringBuilder s = new StringBuilder();
        s.append("{");
        s.append("\"id\": ").append(note.getId()).append(",");
        s.append("\"patientId\": ").append(note.getPatientId()).append(",");
        s.append("\"notes\": ").append("\"").append(note.getNotes()).append("\"").append(",");
        s.append("\"date\": ").append("\"").append(note.getDate().toString()).append("\"");
        s.append("}");

        return s.toString();
    }

    private String notesToJson(List<Note> notes) {

        StringBuilder s = new StringBuilder();
        s.append("[");

        if (notes.size() == 1) {

            s.append(this.noteToJson(notes.get(0)));
        } else {

            for (Note note : notes) {

                if (note == notes.get(notes.size() - 1)) {

                    s.append(this.noteToJson(note));

                } else {

                    s.append(this.noteToJson(note)).append(",");
                }
            }
        }

        s.append("]");
        return s.toString();
    }
}