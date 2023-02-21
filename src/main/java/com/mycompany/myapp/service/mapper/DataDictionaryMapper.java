package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DataDictionaryDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataDictionary} and its DTO {@link DataDictionaryDTO}.
 */
@Mapper(componentModel = "spring", uses = { DataDictionarySimpleMapper.class })
public interface DataDictionaryMapper extends EntityMapper<DataDictionaryDTO, DataDictionary> {
    @Mapping(target = "children", source = "children", qualifiedByName = "nameList")
    @Mapping(target = "parent", source = "parent")
    DataDictionaryDTO toDto(DataDictionary dataDictionary);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    DataDictionary toEntity(DataDictionaryDTO dataDictionaryDTO);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<DataDictionaryDTO> toDtoNameList(List<DataDictionary> dataDictionary);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DataDictionaryDTO toDtoName(DataDictionary dataDictionary);
}
