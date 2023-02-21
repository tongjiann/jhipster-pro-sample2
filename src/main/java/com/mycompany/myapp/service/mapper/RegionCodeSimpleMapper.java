package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.RegionCodeSimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegionCode} and its DTO {@link RegionCodeSimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionCodeSimpleMapper extends EntityMapper<RegionCodeSimpleDTO, RegionCode> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionCodeSimpleDTO toDto(RegionCode regionCode);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionCode toEntity(RegionCodeSimpleDTO regionCodeDTO);
}
