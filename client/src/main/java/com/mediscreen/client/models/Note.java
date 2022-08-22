package com.mediscreen.client.models;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private              BigInteger        id;
    private              Long              patientId;

    private LocalDateTime date;
    private String        notes;

    public Note() {

    }

    public BigInteger getId() {

        return this.id;
    }

    public void setId(BigInteger id) {

        this.id = id;
    }

    public Long getPatientId() {

        return this.patientId;
    }

    public void setPatientId(Long patientId) {

        this.patientId = patientId;
    }

    public LocalDateTime getDate() {

        return this.date;
    }

    public void setDate(CharSequence date) {

        this.date = LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public String getNotes() {

        return this.notes;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }
}
