package com.mediscreen.patient.services;

import com.mediscreen.patient.entities.Patient;
import com.mediscreen.patient.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PatientService {

    public final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }

    public List<Patient> getAll() {

        return this.patientRepository.findAll();
    }

    public Patient getOne(Long id) {

        return this.patientRepository.findById(id)
                                     .orElseThrow(() -> new NoSuchElementException(
                                             String.format("Patient - No found %s", id)));
    }

    public List<Patient> getAll(String lastname, String firstname) {

        return firstname == null ?
                this.patientRepository.findAllByLastname(lastname) :
                this.patientRepository.findAllByLastnameAndFirstname(lastname, firstname);
    }

    public Patient create(Patient patient) {

        if (patient.getId() == null) {

            return this.patientRepository.save(patient);
        }

        throw new EntityExistsException("Patient already exist or id isn't null");
    }

    public Patient update(Patient patient) {

        if (this.patientRepository.existsById(patient.getId())) {

            return this.patientRepository.save(patient);
        }

        throw new NoSuchElementException("Patient not found");
    }
}
