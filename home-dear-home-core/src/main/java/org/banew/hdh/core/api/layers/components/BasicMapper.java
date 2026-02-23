package org.banew.hdh.core.api.layers.components;


import org.banew.hdh.core.api.layers.data.entities.ActionEntity;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.core.api.layers.services.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BasicMapper {
    BasicMapper INSTANCE = Mappers.getMapper(BasicMapper.class);

    UserDto userEntityToDto(UserEntity user);

    LocationDto locationEntityToDto(LocationEntity locationEntity);

    ActionDto actionEntityToDto(ActionEntity action);

    LocationComponentDto componentEntityToDto(ComponentEntity ComponentEntity);
}
