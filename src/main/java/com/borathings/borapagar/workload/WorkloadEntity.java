package com.borathings.borapagar.workload;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "workload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkloadEntity extends AbstractModel {
    @Column(name = "total_workload_completed")
    private Integer totalWorkloadCompleted;

    @Column(name = "total_min_workload")
    private Integer totalMinimumWorkload;

    @Column(name = "pending_workload")
    private Integer pendingWorkload;

    @OneToOne(mappedBy = "workload")
    private StudentEntity student;
}
