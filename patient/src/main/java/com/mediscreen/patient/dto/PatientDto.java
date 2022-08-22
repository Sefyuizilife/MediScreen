package com.mediscreen.patient.dto;

import com.mediscreen.patient.entities.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientDto {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private             Long              id;
    private             String            family;
    private             String            given;
    private             LocalDate         dob;
    private             Character         sex;
    private             String            address;
    private             String            phone;

    public PatientDto() {

    }

    public PatientDto(Patient patient) {

        this.id      = patient.getId();
        this.family  = patient.getLastname();
        this.given   = patient.getFirstname();
        this.dob     = patient.getBirthdate();
        this.sex     = patient.getGender();
        this.address = patient.getAddress();
        this.phone   = patient.getPhone();
    }

    public Patient toPatient() {

        Patient patient = new Patient();

        patient.setId(this.getId());
        patient.setLastname(this.getFamily());
        patient.setFirstname(this.getGiven());
        patient.setBirthdate(LocalDate.parse(this.getDob(), DATE_TIME_FORMATTER));
        patient.setAddress(this.getAddress());
        patient.setGender(this.getSex());
        patient.setPhone(this.getPhone());

        return patient;
    }

    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getFamily() {

        return this.family;
    }

    public void setFamily(String family) {

        this.family = family;
    }

    public String getGiven() {

        return this.given;
    }

    public void setGiven(String given) {

        this.given = given;
    }

    public String getDob() {

        return this.dob.format(DATE_TIME_FORMATTER);
    }

    public void setDob(CharSequence dob) {

        this.dob = LocalDate.parse(dob, DATE_TIME_FORMATTER);
    }

    public char getSex() {

        return this.sex;
    }

    public void setSex(char sex) {

        this.sex = sex;
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
