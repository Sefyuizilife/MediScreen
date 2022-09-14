package com.mediscreen.report.controllers;

import com.mediscreen.report.proxies.NoteProxy;
import com.mediscreen.report.proxies.PatientProxy;
import com.mediscreen.report.services.DiabetesRiskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ReportController.class})
public class ReportControllerTest {

    @MockBean
    PatientProxy patientProxy;
    @MockBean
    NoteProxy    noteProxy;
    @MockBean
    private DiabetesRiskService diabetesRiskService;

    private ReportController    reportController = new ReportController(patientProxy, noteProxy, diabetesRiskService);

    @Test
    public void getDiabetesReport_shouldReturnAssessment_forExistingPatient() {

        //        ResponseEntity<String> diabetesReport = this.reportController.getDiabetesReport(1L);
    }
}
