package com.borathings.borapagar.component.mapper;

import com.borathings.borapagar.component.ComponentEntity;
import com.borathings.borapagar.subject.dto.ComponentDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {

    public ComponentEntity toEntity(com.borathings.borapagar.subject.dto.ComponentDTO componentDTO);

    public ComponentDTO toDto(ComponentEntity componentEntity);

    public ComponentResponseDTO toResponseDTO(ComponentDTO componentDTO);

    public ComponentResponseDTO toResponseDTO(ComponentEntity component);

}
