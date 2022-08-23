package com.mediscreen.report.controllers;

import com.mediscreen.report.models.Note;
import com.mediscreen.report.models.Patient;
import com.mediscreen.report.models.Report;
import com.mediscreen.report.proxies.NoteProxy;
import com.mediscreen.report.proxies.PatientProxy;
import com.mediscreen.report.services.DiabetesRiskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("reports")
public class ReportController {

    private final PatientProxy patientProxy;
    private final NoteProxy    noteProxy;

    private final DiabetesRiskService diabetesRiskService;

    public ReportController(PatientProxy patientProxy, NoteProxy noteProxy, DiabetesRiskService diabetesRiskService) {

        this.patientProxy        = patientProxy;
        this.noteProxy           = noteProxy;
        this.diabetesRiskService = diabetesRiskService;
    }

    @GetMapping(value = "/{patientId}")
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
    //
    //    @GetMapping("familyName/{familyName}")
    //    public ResponseEntity<String> getDiabeticReportByPatientName(@PathVariable String familyName) {
    //
    //        List<String> reports = new ArrayList<>();

    //        List<Patient> patiens = this.patientProxy.search()
    //    }

}
