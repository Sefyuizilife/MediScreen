package com.mediscreen.patient.repositories;


import com.mediscreen.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByLastnameAndFirstname(String lastname, String firstname);

    List<Patient> findAllByLastnameAndFirstname(String lastname, String firstname);
}