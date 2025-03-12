package com.borathings.borapagar.component;

import com.borathings.borapagar.component.dto.ComponentDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ComponentMapper {

    public ComponentEntity toEntity(ComponentDto dto);

    public ComponentDto toDto(ComponentEntity entity);
}
