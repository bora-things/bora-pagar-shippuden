package com.borathings.borapagar.student.interest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentSubjectInterestSemesterDTO(@JsonProperty("year") int year, @JsonProperty("period") int period) {}
