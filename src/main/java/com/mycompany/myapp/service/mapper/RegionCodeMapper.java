package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.RegionCodeDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegionCode} and its DTO {@link RegionCodeDTO}.
 */
@Mapper(componentModel = "spring", uses = { RegionCodeSimpleMapper.class })
public interface RegionCodeMapper extends EntityMapper<RegionCodeDTO, RegionCode> {
    @Mapping(target = "children", source = "children", qualifiedByName = "nameList")
    @Mapping(target = "parent", source = "parent")
    RegionCodeDTO toDto(RegionCode regionCode);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    RegionCode toEntity(RegionCodeDTO regionCodeDTO);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<RegionCodeDTO> toDtoNameList(List<RegionCode> regionCode);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RegionCodeDTO toDtoName(RegionCode regionCode);
}
