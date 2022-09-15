package com.mediscreen.report.utils;

import java.util.List;

public final class DiabetesRules {

    public static final List<String> TRIGGERS = List.of(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Anormal",
            "Cholestérol",
            "Rechute",
            "Réaction",
            "Anticorps"
    );

    public static final Integer AGE_LIMIT = 30;
}
