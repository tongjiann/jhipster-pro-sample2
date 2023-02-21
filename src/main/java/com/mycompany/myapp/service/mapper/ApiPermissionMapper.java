package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ApiPermissionDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApiPermission} and its DTO {@link ApiPermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApiPermissionSimpleMapper.class, DepartmentAuthorityMapper.class })
public interface ApiPermissionMapper extends EntityMapper<ApiPermissionDTO, ApiPermission> {
    @Mapping(target = "children", source = "children", qualifiedByName = "nameList")
    @Mapping(target = "parent", source = "parent")
    ApiPermissionDTO toDto(ApiPermission apiPermission);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    ApiPermission toEntity(ApiPermissionDTO apiPermissionDTO);

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ArrayList<ApiPermissionDTO> toDtoNameList(List<ApiPermission> apiPermission);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ApiPermissionDTO toDtoName(ApiPermission apiPermission);
}
