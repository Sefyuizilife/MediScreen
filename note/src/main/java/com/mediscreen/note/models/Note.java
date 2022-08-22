package com.mediscreen.note.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document
public class Note {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Id
    private             BigInteger        id;
    @NotNull(message = "patientId is required")
    private             Long              patientId;
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date of birth must be before today")
    private             LocalDateTime     date;
    @NotBlank(message = "Note is required")
    private             String            notes;


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
