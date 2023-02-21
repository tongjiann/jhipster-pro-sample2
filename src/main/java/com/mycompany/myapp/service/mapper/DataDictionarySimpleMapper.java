package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DataDictionarySimpleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataDictionary} and its DTO {@link DataDictionarySimpleDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataDictionarySimpleMapper extends EntityMapper<DataDictionarySimpleDTO, DataDictionary> {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DataDictionarySimpleDTO toDto(DataDictionary dataDictionary);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DataDictionary toEntity(DataDictionarySimpleDTO dataDictionaryDTO);
}
