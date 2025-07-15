package com.borathings.borapagar.component;

import com.borathings.borapagar.core.AbstractModel;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "components_embeddings")
@Table(name = "components_embeddings")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class ComponentEmbeddingsEntity extends AbstractModel {
    @Column(name = "content")
    private String content;
    @Column(name = "component_id")
    private Long componentId;
    @Column(name = "embedding_model")
    private String embeddingModel;
    @Column(name = "embedding")
    private List<Integer> embedding;
}
