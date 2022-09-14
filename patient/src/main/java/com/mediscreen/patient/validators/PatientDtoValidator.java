package com.mediscreen.patient.validators;

import com.mediscreen.patient.dto.PatientDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;

public class PatientDtoValidator implements Validator {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z]{3,30}$");

    @Override
    public boolean supports(Class<?> clazz) {

        return PatientDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PatientDto patientDTO = (PatientDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "family", "family.empty", "Family is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "given", "given.empty", "Given is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "dob.empty", "Dob is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "sex.empty", "Sex is required");

        if (patientDTO.getFamily() == null || !PATTERN.matcher(patientDTO.getFamily()).matches()) {

            errors.rejectValue("family", "family.size", "Family must be between 3 and 30 alphanumeric characters.");
        }

        if (patientDTO.getFamily() == null || !PATTERN.matcher(patientDTO.getGiven()).matches()) {

            errors.rejectValue("given", "given.size", "Given must be between 3 and 30 alphanumeric characters.");
        }

        if (!Arrays.asList('M', 'F').contains(patientDTO.getSex())) {

            errors.rejectValue("sex", "sex.error", "Sex value must be \"F\" or \"M\".");
        }

        if (LocalDate.parse(patientDTO.getDob()).isAfter(LocalDate.now())) {

            errors.rejectValue("dob", "dob.format", "Date of birth (dob) isn't valid format");
        }
    }
}
