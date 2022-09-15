package com.mediscreen.report.services;

import com.mediscreen.report.models.Note;
import com.mediscreen.report.models.Report;
import com.mediscreen.report.utils.DiabetesAssessmentResult;
import com.mediscreen.report.utils.DiabetesRules;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DiabetesRiskService {

    public void calculate(Report report) {

        int occurrenceNumber = this.getOccurrenceNumber(report.getNotes());

        report.setDiabeticRisk(DiabetesAssessmentResult.NONE.getResult());

        if (report.getAge() > DiabetesRules.AGE_LIMIT) {

            report.setDiabeticRisk(this.assessmentMoreThanThirtyYears(occurrenceNumber));

        } else {

            report.setDiabeticRisk(this.assessmentThirtyYearsOrLess(report.getSex(), occurrenceNumber));
        }
    }

    private String assessmentMoreThanThirtyYears(int occurrenceNumber) {

        if (occurrenceNumber >= 8) {

            return DiabetesAssessmentResult.EARLY_ONSET.getResult();

        } else if (occurrenceNumber >= 6) {

            return DiabetesAssessmentResult.IN_DANGER.getResult();

        } else if (occurrenceNumber >= 2) {

            return DiabetesAssessmentResult.BORDERLINE.getResult();
        }

        return DiabetesAssessmentResult.NONE.getResult();
    }

    private String assessmentThirtyYearsOrLess(Character sex, int occurrenceNumber) {

        switch (sex) {

            case 'M':

                if (occurrenceNumber >= 5) {

                    return DiabetesAssessmentResult.EARLY_ONSET.getResult();
                } else if (occurrenceNumber >= 3) {

                    return DiabetesAssessmentResult.IN_DANGER.getResult();
                } else if (occurrenceNumber >= 2) {

                    return DiabetesAssessmentResult.BORDERLINE.getResult();
                }

                break;

            case 'F':

                if (occurrenceNumber >= 7) {

                    return DiabetesAssessmentResult.EARLY_ONSET.getResult();
                } else if (occurrenceNumber >= 4) {

                    return DiabetesAssessmentResult.IN_DANGER.getResult();
                } else if (occurrenceNumber >= 2) {

                    return DiabetesAssessmentResult.BORDERLINE.getResult();
                }

                break;

            default:
                throw new IllegalArgumentException("Sex invalid, set 'M' or 'F' value");

        }

        return DiabetesAssessmentResult.NONE.getResult();
    }

    private int getOccurrenceNumber(Collection<Note> notes) {

        Collection<String> temporaryTriggers = new ArrayList<>(DiabetesRules.TRIGGERS);

        notes.stream().map(Note::getNotes).forEach(note -> {
            DiabetesRules.TRIGGERS.forEach(trigger -> {
                if (note.toLowerCase().contains(trigger.toLowerCase())) {

                    temporaryTriggers.remove(trigger);
                }
            });
        });


        return DiabetesRules.TRIGGERS.size() - temporaryTriggers.size();
    }
}