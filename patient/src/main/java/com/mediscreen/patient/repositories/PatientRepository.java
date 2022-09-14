package com.mediscreen.patient.repositories;


import com.mediscreen.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllByLastname(String lastname);

    List<Patient> findAllByLastnameAndFirstname(String lastname, String firstname);
}