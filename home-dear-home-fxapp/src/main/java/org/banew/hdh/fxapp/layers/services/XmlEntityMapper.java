package org.banew.hdh.fxapp.layers.services;

import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.layers.repo.xml.XmlLocation;
import org.banew.hdh.fxapp.layers.repo.xml.XmlLocationComponent;
import org.banew.hdh.fxapp.layers.repo.xml.XmlUserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface XmlEntityMapper {
    XmlEntityMapper INSTANCE = Mappers.getMapper(XmlEntityMapper.class);

    UserEntity userXmlToEntity(XmlUserInfo xmlUserInfo);

    XmlUserInfo userEntityToXml(UserEntity user);

    LocationEntity locationXmlToEntity(XmlLocation l);

    ComponentEntity componentXmlToEntity(XmlLocationComponent xmlLocationComponent);

    XmlLocation locationEntityToXml(LocationEntity location);
}
