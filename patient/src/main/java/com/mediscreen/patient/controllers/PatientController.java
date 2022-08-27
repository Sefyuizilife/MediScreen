package com.mediscreen.patient.controllers;

import com.mediscreen.patient.dto.PatientDto;
import com.mediscreen.patient.entities.Patient;
import com.mediscreen.patient.services.PatientService;
import com.mediscreen.patient.validators.PatientDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


/**
 * Controller specifically for managing the {@link Patient}.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger         LOGGER              = LoggerFactory.getLogger(PatientController.class);
    private final        Validator      patientDtoValidator = new PatientDtoValidator();
    private final        PatientService patientService;

    /**
     * /** Create a new instance of this {@link PatientController}. This will be done automatically by SpringBoot with
     * dependencies injection.
     *
     * @param patientService
     *         an instance of {@link PatientService}.
     */
    public PatientController(PatientService patientService) {

        this.patientService = patientService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        binder.setValidator(new PatientDtoValidator());
    }

    /**
     * Getting all registered {@link Patient}.
     *
     * @return a {@link List} of {@link Patient}.
     */
    @GetMapping()
    public ResponseEntity<List<PatientDto>> browse() {

        LOGGER.info("GET: /patients");

        return ResponseEntity.ok(
                this.patientService.getAll().stream().map(PatientDto::new).collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> search(@RequestParam String lastname, @RequestParam(required = false) String firstname) {

        LOGGER.info("GET: /patients?lastname={}&firstname={}", lastname, firstname);

        return ResponseEntity.ok(this.patientService.getAll(lastname, firstname)
                                                    .stream()
                                                    .map(PatientDto::new).collect(Collectors.toList())
        );
    }

    /**
     * Get patient registered to ID.
     *
     * @return a {@link Patient}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> read(@PathVariable Long id) {

        LOGGER.info("GET: /patients/{}", id);

        try {

            return ResponseEntity.ok(new PatientDto(this.patientService.getOne(id)));

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Register a new {@link Patient}
     *
     * @param patientDTO
     *         to register.
     *
     * @return a {@link Patient}
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<PatientDto> create(@Valid PatientDto patientDTO, Errors errors) {

        LOGGER.info("POST: /patients");

        try {

            if (errors.hasErrors()) {

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(
                    new PatientDto(this.patientService.create(patientDTO.toPatient()))
            );

        } catch (EntityExistsException e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Save changes to an {@link Patient} matching ID.
     *
     * @param patientDTO
     *         to register.
     *
     * @return a {@link Patient}.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> update(@Valid PatientDto patientDTO, Errors errors) {

        LOGGER.info("PUT: /patients");

        try {

            if (errors.hasErrors()) {

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Patient updatedPatient = this.patientService.update(patientDTO.toPatient());

            return ResponseEntity.ok(new PatientDto(updatedPatient));

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
