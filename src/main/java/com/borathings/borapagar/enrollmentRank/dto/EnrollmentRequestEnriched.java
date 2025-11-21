package com.borathings.borapagar.enrollmentRank.dto;

import java.math.BigDecimal;
import java.util.Comparator;

public record EnrollmentRequestEnriched(
        EnrollmentRequestDTO originalRequest, boolean hasDroppedOrFailedByAbsence, BigDecimal iea) {

    public static Comparator<EnrollmentRequestEnriched> getTieBreakerComparator() {

        Comparator<EnrollmentRequestEnriched> byHistory =
                Comparator.comparing(EnrollmentRequestEnriched::hasDroppedOrFailedByAbsence);

        Comparator<EnrollmentRequestEnriched> byIea =
                Comparator.comparing(EnrollmentRequestEnriched::iea).reversed();

        return byHistory.thenComparing(byIea);
    }
}
