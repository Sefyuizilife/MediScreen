package com.mediscreen.report.controllers;

import com.mediscreen.report.models.Note;
import com.mediscreen.report.models.Patient;
import com.mediscreen.report.proxies.NoteProxy;
import com.mediscreen.report.proxies.PatientProxy;
import com.mediscreen.report.services.DiabetesRiskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    private static final Patient             PATIENT = new Patient();
    private static final List<Note>          NOTES   = new ArrayList<>();
    @MockBean
    private static       PatientProxy        PATIENT_PROXY;
    @MockBean
    private static       NoteProxy           NOTE_PROXY;
    private final        MockMvc             mockMvc;
    @MockBean
    private              DiabetesRiskService diabetesRiskService;

    public ReportControllerTest(@Autowired MockMvc mockMvc) {

        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public static void setUp() {

        PATIENT.setFamily("PatientTest");
        PATIENT.setGiven("None");
        PATIENT.setDob("1980-05-15");
        PATIENT.setSex('M');

        for (int i = 0 ; i <= 2 ; i++) {
            Note note = new Note();
            note.setId(new BigInteger(String.valueOf(i) + 1));
            note.setPatientId(1L);
            note.setDate(LocalDateTime.now().withSecond(2).withNano(0).toString());
            note.setNotes(String.format("My note %s", i));

            NOTES.add(note);
        }


    }

    @Test
    public void getDiabetesReport_shouldReturnAssessment_forExistingPatient() throws Exception {

        when(PATIENT_PROXY.read(any())).thenReturn(PATIENT);
        when(NOTE_PROXY.browseByPatientId(any())).thenReturn(NOTES);

        this.mockMvc.perform(get("/assess/{id}", 1L)).andExpect(status().isOk());
    }

    @Test
    public void getDiabeticReportByPatientName() throws Exception {

        List<Patient> patients = new ArrayList<>();

        for (int i = 0 ; i <= 2 ; i++) {
            patients.add(PATIENT);
        }

        when(PATIENT_PROXY.search(any(), any())).thenReturn(patients);
        when(NOTE_PROXY.browseByPatientId(any())).thenReturn(NOTES);

        this.mockMvc.perform(post("/assess/familyName").param("familyName", "test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(3)));
    }
}
