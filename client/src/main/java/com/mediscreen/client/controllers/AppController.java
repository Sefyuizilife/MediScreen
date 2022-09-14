package com.mediscreen.client.controllers;

import com.mediscreen.client.models.Note;
import com.mediscreen.client.models.Patient;
import com.mediscreen.client.proxies.NoteProxy;
import com.mediscreen.client.proxies.PatientProxy;
import com.mediscreen.client.proxies.ReportProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Controller
@RequestMapping("app/patients")
public class AppController {

    private static final Logger       LOGGER = LoggerFactory.getLogger(AppController.class);
    private final        PatientProxy patientProxy;
    private final        NoteProxy    noteProxy;

    private final ReportProxy reportProxy;

    public AppController(PatientProxy patientProxy, NoteProxy noteProxy, ReportProxy reportProxy) {

        this.noteProxy    = noteProxy;
        this.patientProxy = patientProxy;
        this.reportProxy  = reportProxy;
    }

    @GetMapping()
    public String browse(Model model, @RequestParam(required = false) String lastname, @RequestParam(required = false) String firstname, @RequestParam(required = false) String message) {

        if (lastname != null && !lastname.isEmpty() && firstname != null && !firstname.isEmpty()) {

            LOGGER.info("GET: app/patients?lastname={}&firstname={}", lastname, firstname);

            model.addAttribute("patients", this.patientProxy.search(lastname, firstname));
        } else {

            LOGGER.info("GET: app/patients");

            model.addAttribute("patients", this.patientProxy.browse());
        }

        model.addAttribute("message", message);

        model.addAttribute("lastname", lastname);
        model.addAttribute("firstname", firstname);
        model.addAttribute("patient", new Patient());

        return "patients";
    }

    @GetMapping("/{id}")
    public String read(Model model, @PathVariable() Long id, @RequestParam(required = false) boolean withReport) {

        LOGGER.info("GET: /app/patients/{}", id);

        model.addAttribute("patient", this.patientProxy.read(id));
        model.addAttribute("notes", this.noteProxy.browseByPatientId(id)
                                                  .stream()
                                                  .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                                                  .collect(Collectors.toList()));

        if (withReport) {

            String body = this.reportProxy.getDiabetesReport(id);

            if (body != null) {
                this.passingOnTheAssessment(model, body);
            }

        } else {

            model.addAttribute("report", "ToBeCalculated");
        }

        return "patient";
    }

    private void passingOnTheAssessment(Model model, String body) {

        if (body.contains("None")) {

            model.addAttribute("report", "None");

        } else if (body.contains("Danger")) {

            model.addAttribute("report", "Danger");

        } else if (body.contains("Borderline")) {

            model.addAttribute("report", "Borderline");

        } else if (body.contains("Early")) {

            model.addAttribute("report", "Early");
        }

        model.addAttribute("reportValue", body);
    }

    @PostMapping()
    public String storePatient(RedirectAttributes redirectAttributes, @ModelAttribute Patient patient) {

        LOGGER.info("POST: /app/patients");

        this.patientProxy.create(patient);

        redirectAttributes.addAttribute("message", "create").addFlashAttribute("message", "create");

        return "redirect:/app/patients";
    }

    @PutMapping("/{id}")
    public String update(RedirectAttributes redirectAttributes, @PathVariable Long id, @ModelAttribute Patient patient) {

        LOGGER.info("POST: /app/patients/{}", id);

        this.patientProxy.update(patient, id);

        redirectAttributes.addAttribute("message", "update").addFlashAttribute("message", "update");

        return "redirect:/app/patients";
    }

    @PostMapping("/{id}/notes")
    public String storeNote(String notes, @PathVariable Long id) {

        LOGGER.info("POST: app/patients/{}/notes", id);

        Note note = new Note();
        note.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        note.setNotes(notes);
        note.setPatientId(id);

        this.noteProxy.create(note);

        return "redirect:/app/patients/{id}";
    }


    @PutMapping("/{id}/notes/{nId}")
    public String updateNoteByPatient(@PathVariable Long id, @PathVariable BigInteger nId, @ModelAttribute Note note) {

        LOGGER.info("PUT: app/patients/{}/notes/{}", id, nId);

        this.noteProxy.update(nId, note);

        return "redirect:/app/patients/" + id;
    }
}
