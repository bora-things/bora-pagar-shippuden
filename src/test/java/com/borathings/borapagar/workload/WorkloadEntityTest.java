package com.borathings.borapagar.workload;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class WorkloadEntityTest {


    @Test
    public void EntityBuilderTest() {
        WorkloadEntity workload = WorkloadEntity.builder()
                .totalMinimumWorkload(30)
                .totalWorkloadCompleted(50)
                .pendingWorkload(20)
                .build();

        assertThat(30).isEqualTo(workload.getTotalMinimumWorkload());
        assertThat(50).isEqualTo(workload.getTotalWorkloadCompleted());
        assertThat(20).isEqualTo(workload.getPendingWorkload());

    }


}
