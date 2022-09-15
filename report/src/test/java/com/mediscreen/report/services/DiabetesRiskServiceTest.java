package com.mediscreen.report.services;

import com.mediscreen.report.models.Note;
import com.mediscreen.report.models.Patient;
import com.mediscreen.report.models.Report;
import com.mediscreen.report.utils.DiabetesAssessmentResult;
import com.mediscreen.report.utils.DiabetesRules;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DiabetesRiskService.class})
public class DiabetesRiskServiceTest {

    private static DiabetesRiskService DIABETES_RISK_SERVICE;

    @BeforeAll
    public static void setUp() {

        DIABETES_RISK_SERVICE = new DiabetesRiskService();
    }

    @Test
    public void calculate_shouldCorrectNumberOfReportResults() {

        List<Report> reports = this.generateRapports();

        for (Report report : reports) {

            DIABETES_RISK_SERVICE.calculate(report);
        }

        Assertions.assertThat(
                          reports.stream()
                                 .map(Report::getDiabeticRisk)
                                 .filter(result -> result.equals(DiabetesAssessmentResult.NONE.getResult()))
                                 .count())
                  .isEqualTo(2);
        Assertions.assertThat(
                          reports.stream()
                                 .map(Report::getDiabeticRisk)
                                 .filter(result -> result.equals(DiabetesAssessmentResult.BORDERLINE.getResult()))
                                 .count())
                  .isEqualTo(6);
        Assertions.assertThat(
                          reports.stream()
                                 .map(Report::getDiabeticRisk)
                                 .filter(result -> result.equals(DiabetesAssessmentResult.IN_DANGER.getResult()))
                                 .count())
                  .isEqualTo(4);
        Assertions.assertThat(
                          reports.stream()
                                 .map(Report::getDiabeticRisk)
                                 .filter(result -> result.equals(DiabetesAssessmentResult.EARLY_ONSET.getResult()))
                                 .count())
                  .isEqualTo(8);
    }

    @Test
    public void calculate_shouldThrowException_forBadSexValue() {

        Patient patient = new Patient();
        patient.setSex('A');
        patient.setDob(LocalDate.now().minusYears(30L).toString());

        Report report = new Report(patient, new ArrayList<>());

        Assertions.assertThatThrownBy(() -> DIABETES_RISK_SERVICE.calculate(report))
                  .isInstanceOf(IllegalArgumentException.class)
                  .hasMessageContaining("Sex invalid, set 'M' or 'F' value");
    }

    private List<Report> generateRapports() {

        List<Report> reports = new ArrayList<>();

        for (int i = 0 ; i < DiabetesRules.TRIGGERS.size() ; i++) {

            Patient patient = new Patient();
            patient.setId(1L + (long) i);
            patient.setFamily("Test");
            patient.setGiven("None");
            patient.setSex((patient.getId() % 2 == 0) ? ('M') : ('F'));
            patient.setDob(LocalDate.now().minusYears(31L).toString());

            List<Note> notes = new ArrayList<>();

            Note note = new Note();
            note.setId(BigInteger.valueOf(1));
            note.setPatientId(patient.getId());
            note.setDate(LocalDateTime.now().withSecond(2).withNano(0).toString());
            note.setNotes(String.join(" ", DiabetesRules.TRIGGERS.subList(0, patient.getId().intValue())));

            notes.add(note);

            reports.add(new Report(patient, notes));
        }

        for (int i = 0 ; i < DiabetesRules.TRIGGERS.size() ; i++) {

            Patient patient = new Patient();
            patient.setId(1L + (long) i);
            patient.setFamily("Test");
            patient.setGiven("None");
            patient.setSex((patient.getId() % 2 == 0) ? ('M') : ('F'));
            patient.setDob(LocalDate.now().toString());

            List<Note> notes = new ArrayList<>();

            Note note = new Note();
            note.setId(BigInteger.valueOf(1));
            note.setPatientId(patient.getId());
            note.setDate(LocalDateTime.now().withSecond(2).withNano(0).toString());
            note.setNotes(String.join(" ", DiabetesRules.TRIGGERS.subList(0, patient.getId().intValue())));

            notes.add(note);

            reports.add(new Report(patient, notes));
        }

        return reports;
    }
}