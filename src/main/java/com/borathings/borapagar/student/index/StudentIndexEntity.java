package com.borathings.borapagar.student.index;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "indexes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StudentIndexEntity extends AbstractModel {

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private StudentEntity student;

	@NotNull
	@Column
	private String value;

	@NotNull
	@Column
	private String name;

	@NotNull
	@Column(name = "sigaa_index_id")
	// ID do indice mapeado no SIGAA
	private Long indexId;

	@NotNull
	@Column(name = "sigaa_student_index_id")
	private Long studentIndexId;
}
