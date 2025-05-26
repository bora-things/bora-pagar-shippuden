package com.borathings.borapagar.workload;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class WorkloadDtoTests {

    @Test
    public void testWorkloadDtoCreation() {
        WorkloadDto dto = new WorkloadDto(50, 30, 20, 1, 1001, "GRAD");

        assertThat(dto.totalWorkloadCompleted()).isEqualTo(50);
        assertThat(dto.totalMinimumWorkload()).isEqualTo(30);
        assertThat(dto.pendingWorkload()).isEqualTo(20);
        assertThat(dto.idCurso()).isEqualTo(1);
        assertThat(dto.idDiscente()).isEqualTo(1001);
        assertThat(dto.siglaNivel()).isEqualTo("GRAD");
    }
}
