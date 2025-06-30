package com.borathings.borapagar.component.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ComponentDetailsDTO(
        @JsonProperty("id-componente")
          String idComponent,
          @JsonProperty("objetivos")
          String objectives,
          @JsonProperty("conteudo")
          String content) {
}
