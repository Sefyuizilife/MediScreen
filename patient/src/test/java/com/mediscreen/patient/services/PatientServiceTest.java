package com.mediscreen.patient.services;

import com.mediscreen.patient.entities.Patient;
import com.mediscreen.patient.repositories.PatientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PatientService.class})
public class PatientServiceTest {

    private final PatientService    patientService;
    private final List<Patient>     patients = new ArrayList<>();
    @MockBean
    private       PatientRepository patientRepository;

    public PatientServiceTest(@Autowired PatientService patientService) {

        this.patientService = patientService;
    }

    @BeforeEach
    public void setUpPerTest() {

        for (int i = 0 ; i <= 2 ; i++) {
            Patient patient = new Patient();
            patient.setId(i + 1l);
            patient.setLastname("Lastname" + i);
            patient.setFirstname("Firstname" + i);
            patient.setBirthdate(LocalDate.now());
            patient.setGender('M');
            patient.setAddress("Address" + i);
            patient.setPhone(String.valueOf(i * 1000000));

            this.patients.add(patient);
        }
    }

    @Test
    public void getAll_shouldReturnAllPatients() {

        when(this.patientRepository.findAll()).thenReturn(this.patients);

        List<Patient> results = this.patientService.getAll();

        Assertions.assertThat(results).isSameAs(this.patients);
    }

    @Test
    public void getAll_shouldReturnAllPatientCorresponding_forFirstnameAndLastnameMatch() {

        Patient       patient         = this.patients.get(0);
        List<Patient> expectedResults = Collections.singletonList(patient);

        when(this.patientRepository.findAllByLastnameAndFirstname(
                patient.getLastname(), patient.getFirstname())).thenReturn(expectedResults);

        List<Patient> results = this.patientService.getAll(patient.getLastname(), patient.getFirstname());

        Assertions.assertThat(results).isSameAs(expectedResults);
    }

    @Test
    public void getAll_shouldReturnAllPatientCorresponding_forFirstnameAndLastnameNotMatch() {

        String        lastname        = "unknow";
        String        firstname       = "firstname";
        List<Patient> expectedResults = Collections.emptyList();

        when(this.patientRepository.findAllByLastnameAndFirstname(
                lastname, firstname)).thenReturn(expectedResults);

        List<Patient> results = this.patientService.getAll(lastname, firstname);

        Assertions.assertThat(results).isSameAs(expectedResults);
    }

    @Test
    public void getOne_shouldReturnOnePatient_forIdExists() {

        Patient           patient  = this.patients.get(0);
        Optional<Patient> oPatient = Optional.of(patient);
        when(this.patientRepository.findById(patient.getId())).thenReturn(oPatient);

        Optional<Patient> result = this.patientRepository.findById(patient.getId());
        Assertions.assertThat(result).isSameAs(oPatient);
    }

    @Test
    public void getOne_shouldThrowException_forIdNotExists() {

        Optional<Patient> oPatient = Optional.empty();
        when(this.patientRepository.findById(99L)).thenReturn(oPatient);

        Assertions.assertThatThrownBy(() -> this.patientService.getOne(99L))
                  .isInstanceOf(NoSuchElementException.class)
                  .hasMessageContaining(String.format("Patient - No found %s", 99L));
    }

    @Test
    public void update_shouldReturnUpdatedPatient_forExistsPatient() {

        Patient patient = this.patients.get(0);
        when(this.patientRepository.existsById(patient.getId())).thenReturn(true);
        when(this.patientRepository.save(patient)).thenReturn(patient);

        Assertions.assertThat(this.patientService.update(patient)).isSameAs(patient);
    }

    @Test
    public void update_shouldThrowException_forNotExistsPatient() {

        Patient patient = this.patients.get(0);
        when(this.patientRepository.existsById(patient.getId())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
            this.patientService.update(patient);
        }).isInstanceOf(NoSuchElementException.class).hasMessageContaining("Patient not found");
    }

    @Test
    public void create_shouldReturnCreatedPatient_forNewPatient() {

        Patient newPatient = this.patients.get(0);
        newPatient.setId(null);

        when(this.patientRepository.save(newPatient)).thenReturn(this.patients.get(0));

        Assertions.assertThat(this.patientService.create(newPatient)).isSameAs(this.patients.get(0));
    }

    @Test
    public void create_shouldThrowException_forPatientExisting() {

        Patient newPatient = this.patients.get(0);

        when(this.patientRepository.existsById(newPatient.getId())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            this.patientService.create(newPatient);
        }).isInstanceOf(EntityExistsException.class).hasMessageContaining("Patient already exist");
    }
}