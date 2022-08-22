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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "family", "family.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "given", "given.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "dob.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "sex.empty");

        if (!PATTERN.matcher(patientDTO.getFamily()).matches()) {

            errors.rejectValue("family", "family.size", "Lastname must be between 3 and 30 alphanumeric characters.");
        }

        if (!PATTERN.matcher(patientDTO.getGiven()).matches()) {

            errors.rejectValue("given", "given.size", "Firstname must be between 3 and 30 alphanumeric characters.");
        }

        if (!Arrays.asList('M', 'F').contains(patientDTO.getSex())) {

            errors.rejectValue("sex", "sex.error", "The sex value must be \"F\" or \"M\".");
        }

        if (LocalDate.parse(patientDTO.getDob()).isAfter(LocalDate.now())) {

            errors.rejectValue("dob","dob.after", "The date of birth cannot be after the current date.");
        }
    }
}
