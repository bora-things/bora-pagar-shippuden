package com.borathings.borapagar.workload;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "carga_horaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class WorkloadEntity extends AbstractModel {
    @Column(name = "ch_total_integralizada")
    private Integer chTotalIntegralizada;

    @Column(name = "ch_total_min")
    private Integer chTotalMin;

    @Column(name = "ch_total_pendente")
    private Integer chTotalPendente;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
