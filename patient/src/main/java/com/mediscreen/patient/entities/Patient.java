package com.mediscreen.patient.entities;

import com.mediscreen.patient.dto.PatientDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 30, message = "Lastname must be between 1 and 30 characters")
    @NotBlank(message = "Lastname is required")
    private String    lastname;
    @Size(min = 1, max = 30, message = "Firstname must be between 1 and 30 characters")
    @NotBlank(message = "Firstname is required")
    private String    firstname;
    @PastOrPresent(message = "Date of birth must be before today")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birthdate;
    @NotNull(message = "Gender is required")
    private Character gender;
    private String    address;
    private String    phone;

    public Patient() {

    }

    public Patient(PatientDto patientDTO) {

        this.setId(patientDTO.getId());
        this.setLastname(patientDTO.getFamily());
        this.setFirstname(patientDTO.getGiven());
        this.setBirthdate(LocalDate.parse(patientDTO.getDob(), PatientDto.DATE_TIME_FORMATTER));
        this.setAddress(patientDTO.getAddress());
        this.setGender(patientDTO.getSex());
        this.setPhone(patientDTO.getPhone());
    }

    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getLastname() {

        return this.lastname;
    }

    public void setLastname(String lastname) {

        this.lastname = lastname;
    }

    public String getFirstname() {

        return this.firstname;
    }

    public void setFirstname(String firstname) {

        this.firstname = firstname;
    }

    public LocalDate getBirthdate() {

        return this.birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {

        this.birthdate = birthdate;
    }

    public char getGender() {

        return this.gender;
    }

    public void setGender(char gender) {

        this.gender = gender;
    }

    public String getAddress() {

        return this.address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getPhone() {

        return this.phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }
}
