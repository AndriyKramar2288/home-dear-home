package org.banew.hdh.core.api.layers.components;


import org.banew.hdh.core.api.layers.data.entities.ActionEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.core.api.layers.services.dto.ActionDto;
import org.banew.hdh.core.api.layers.services.dto.DetailedUserDto;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.banew.hdh.core.api.layers.services.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BasicMapper {
    BasicMapper INSTANCE = Mappers.getMapper(BasicMapper.class);

    @Mapping(target = "locations", source = "locations")
    @Mapping(target = "userDto", source = "user")
    DetailedUserDto userEntityToDetailedDto(UserEntity user);

    UserDto userEntityToDto(UserEntity user);

    LocationDto locationEntityToDto(LocationEntity locationEntity);

    ActionDto actionEntityToDto(ActionEntity action);
}
