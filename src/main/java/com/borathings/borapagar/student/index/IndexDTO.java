package com.borathings.borapagar.student.index;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IndexDTO(
		@JsonProperty("id-discente") Long studentId,
		@JsonProperty("id-indice-academico") Long indexId,
		@JsonProperty("id-indice-discente") Long studentIndexId,
		@JsonProperty("valor-indice") String value) {
}
