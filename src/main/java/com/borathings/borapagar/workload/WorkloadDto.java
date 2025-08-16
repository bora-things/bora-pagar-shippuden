package com.borathings.borapagar.workload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WorkloadDto(
        @JsonProperty("ch-total-integralizada") Integer totalWorkloadCompleted,
        @JsonProperty("ch-total-minima") Integer totalMinimumWorkload,
        @JsonProperty("ch-total-pendente") Integer pendingWorkload) {}
