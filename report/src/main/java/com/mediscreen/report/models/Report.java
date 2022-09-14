package com.mediscreen.report.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class Report {

    private final String     fullName;
    private final Integer    age;
    private final Character  sex;
    private final List<Note> notes;

    private String diabeticRisk = "UNKNOWN";

    public Report(Patient patient, List<Note> notes) {

        this.fullName = String.format("%s %s", patient.getFamily(), patient.getGiven());
        this.age      = Period.between(patient.getDob(), LocalDate.now()).getYears();
        this.sex      = patient.getSex();
        this.notes    = notes;
    }

    public String getFullName() {

        return this.fullName;
    }

    public Integer getAge() {

        return this.age;
    }

    public Character getSex() {

        return this.sex;
    }

    public List<Note> getNotes() {

        return this.notes;
    }

    public String getDiabeticRisk() {

        return this.diabeticRisk;
    }

    public void setDiabeticRisk(String diabeticRisk) {

        this.diabeticRisk = diabeticRisk;
    }
}
