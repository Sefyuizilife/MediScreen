package com.mediscreen.patient.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.patient.dto.PatientDto;
import com.mediscreen.patient.entities.Patient;
import com.mediscreen.patient.services.PatientService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    private final MockMvc        mockMvc;
    private final ObjectMapper   objectMapper = new ObjectMapper();
    private final List<Patient>  patients     = new ArrayList<>();
    @MockBean
    private       PatientService patientService;

    public PatientControllerTest(@Autowired MockMvc mockMvc) {

        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUpPerTest() {

        for (int i = 0 ; i <= 2 ; i++) {
            Patient patient = new Patient();
            patient.setId(i + 1l);
            patient.setLastname("Lastname");
            patient.setFirstname("Firstname");
            patient.setBirthdate(LocalDate.now());
            patient.setGender('M');
            patient.setAddress("Address" + i);
            patient.setPhone(String.valueOf(i * 1000000));

            this.patients.add(patient);
        }
    }

    @Test
    public void browse_shouldAllPatients() throws Exception {


        when(this.patientService.getAll()).thenReturn(this.patients);
        String resultExpected = this.objectMapper.writeValueAsString(
                this.patients.stream().map(PatientDto::new).collect(Collectors.toList()));

        this.mockMvc.perform(get("/patients/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(resultExpected));
    }

    @Test
    public void search_shouldPatientSought_ForCorrectFirstnameAndLastname() throws Exception {

        Patient patient = this.patients.get(0);
        when(this.patientService.getAll(patient.getLastname(), patient.getFirstname()))
                .thenReturn(Collections.singletonList(patient));
        String resultExpected = this.objectMapper.writeValueAsString(
                Collections.singletonList(new PatientDto(patient)));

        this.mockMvc.perform(
                    get(
                            "/patients/search?lastname={lastname}&firstname={firstname}", patient.getLastname(),
                            patient.getFirstname()
                    ))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(resultExpected));
    }

    @Test
    public void read_shouldPatientCorresponding_forCorrectId() throws Exception {

        Patient patient        = this.patients.get(0);
        String  resultExpected = this.objectMapper.writeValueAsString(new PatientDto(patient));
        when(this.patientService.getOne(patient.getId())).thenReturn(patient);

        this.mockMvc.perform(get("/patients/{id}", patient.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(resultExpected));
    }

    @Test
    public void read_should404_forIncorrectId() throws Exception {

        when(this.patientService.getOne(99l)).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(get("/patients/{id}", 99))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void create_should400_forBadRequest() throws Exception {

        Patient patient = new Patient();
        patient.setLastname("");
        patient.setFirstname("");
        patient.setGender('R');
        patient.setBirthdate(LocalDate.now().plusDays(1));
        patient.setAddress("4561561 rgfergerg erg ergerggerg");
        patient.setPhone("6515 651 651");


        PatientDto patientDTO = new PatientDto(patient);

        this.mockMvc.perform(post("/patients")
                                     .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                     .param("family", patientDTO.getFamily())
                                     .param("given", patientDTO.getGiven())
                                     .param("dob", patientDTO.getDob())
                                     .param("sex", String.valueOf(patientDTO.getSex()))
                                     .param("address", patientDTO.getAddress())
                                     .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void update_should400_forBadRequest() throws Exception {

        Patient patient = new Patient();
        patient.setId(1l);
        patient.setLastname("");
        patient.setFirstname("");
        patient.setGender('R');
        patient.setBirthdate(LocalDate.now().plusDays(1));
        patient.setAddress("4561561 rgfergerg erg ergerggerg");
        patient.setPhone("6515 651 651");


        PatientDto patientDTO = new PatientDto(patient);

        this.mockMvc.perform(put("/patients/{id}", patient.getId())
                                     .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                     .param("family", patientDTO.getFamily())
                                     .param("given", patientDTO.getGiven())
                                     .param("dob", patientDTO.getDob())
                                     .param("sex", String.valueOf(patientDTO.getSex()))
                                     .param("address", patientDTO.getAddress())
                                     .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void create_shouldRegisterPatient_forCorrectData() throws Exception {

        Patient patient = this.patients.get(0);
        patient.setId(99l);
        PatientDto patientDTO = new PatientDto(patient);

        when(this.patientService.create(any(Patient.class))).thenReturn(patient);

        this.mockMvc.perform(post("/patients")
                                     .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                     .param("family", patientDTO.getFamily())
                                     .param("given", patientDTO.getGiven())
                                     .param("dob", patientDTO.getDob())
                                     .param("sex", String.valueOf(patientDTO.getSex()))
                                     .param("address", patientDTO.getAddress())
                                     .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json(this.objectMapper.writeValueAsString(new PatientDto(patient))));
    }

    @Test
    public void create_shouldRegisterPatient_forIncorrectData() throws Exception {

        Patient patient = this.patients.get(0);
        patient.setId(99l);
        PatientDto patientDTO = new PatientDto(patient);

        when(this.patientService.create(any(Patient.class))).thenThrow(EntityExistsException.class);

        this.mockMvc.perform(post("/patients")
                                     .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                     .param("id", patientDTO.getId().toString())
                                     .param("family", patientDTO.getFamily())
                                     .param("given", patientDTO.getGiven())
                                     .param("dob", patientDTO.getDob())
                                     .param("sex", String.valueOf(patientDTO.getSex()))
                                     .param("address", patientDTO.getAddress())
                                     .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void update_shouldPatientUpdated_ForExistingPatient() throws Exception {

        Patient patient = this.patients.get(0);
        patient.setId(99l);
        PatientDto patientDTO = new PatientDto(patient);
        when(this.patientService.update(any(Patient.class))).thenReturn(patient);

        this.mockMvc.perform(
                    put("/patients/{id}", patient.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                          .param("id", patientDTO.getId().toString())
                                                          .param("family", patientDTO.getFamily())
                                                          .param("given", patientDTO.getGiven())
                                                          .param("dob", patientDTO.getDob())
                                                          .param("sex", String.valueOf(patientDTO.getSex()))
                                                          .param("address", patientDTO.getAddress())
                                                          .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(this.objectMapper.writeValueAsString(new PatientDto(patient))));

    }

    @Test
    public void update_shouldReturnNotFound_ForNotExistingPatient() throws Exception {

        Patient    patient    = this.patients.get(0);
        PatientDto patientDTO = new PatientDto(patient);
        when(this.patientService.update(any(Patient.class))).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(
                    put("/patients/{id}", patient.getId()).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                          .param("id", patientDTO.getId().toString())
                                                          .param("family", patientDTO.getFamily())
                                                          .param("given", patientDTO.getGiven())
                                                          .param("dob", patientDTO.getDob())
                                                          .param("sex", String.valueOf(patientDTO.getSex()))
                                                          .param("address", patientDTO.getAddress())
                                                          .param("phone", patientDTO.getPhone()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
    }
}