package com.mediscreen.report.controllers;

import com.mediscreen.report.models.Note;
import com.mediscreen.report.models.Patient;
import com.mediscreen.report.models.Report;
import com.mediscreen.report.proxies.NoteProxy;
import com.mediscreen.report.proxies.PatientProxy;
import com.mediscreen.report.services.DiabetesRiskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("assess/")
public class ReportController {

    private final PatientProxy patientProxy;
    private final NoteProxy    noteProxy;

    private final DiabetesRiskService diabetesRiskService;

    public ReportController(PatientProxy patientProxy, NoteProxy noteProxy, DiabetesRiskService diabetesRiskService) {

        this.patientProxy        = patientProxy;
        this.noteProxy           = noteProxy;
        this.diabetesRiskService = diabetesRiskService;
    }

    @GetMapping("id/{patientId}")
    public ResponseEntity<String> getDiabetesReportByPatientId(@PathVariable Long patientId) {

        Patient    patient = this.patientProxy.read(patientId);
        List<Note> notes   = this.noteProxy.browseByPatientId(patientId).getBody();

        Report report = new Report(patient, notes);

        this.diabetesRiskService.calculate(report);

        return new ResponseEntity<>(
                String.format(
                        "Patient: %s (age %s) diabetes assessment is: %s",
                        report.getFullName(),
                        report.getAge(),
                        report.getDiabeticRisk()
                ), HttpStatus.OK);
    }

    @PostMapping("familyName/{familyName}")
    public ResponseEntity<List<String>> getDiabeticReportByPatientName(@PathVariable String familyName) {

        List<Report> reports = new ArrayList<>();

        List<Patient> patients = this.patientProxy.search(familyName, null);

        patients.forEach(patient -> reports.add(
                new Report(patient, this.noteProxy.browseByPatientId(patient.getId()).getBody())));

        reports.forEach(this.diabetesRiskService::calculate);

        List<String> results = new ArrayList<>();

        reports.forEach(report -> results.add(String.format(
                "Patient: %s (age %s) diabetes assessment is: %s",
                report.getFullName(),
                report.getAge(),
                report.getDiabeticRisk()
        )));

        return ResponseEntity.ok(results);
    }
}
