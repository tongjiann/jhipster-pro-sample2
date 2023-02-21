package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DepartmentAuthorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepartmentAuthority} and its DTO {@link DepartmentAuthorityDTO}.
 */
@Mapper(componentModel = "spring", uses = { DepartmentMapper.class })
public interface DepartmentAuthorityMapper extends EntityMapper<DepartmentAuthorityDTO, DepartmentAuthority> {
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    DepartmentAuthorityDTO toDto(DepartmentAuthority departmentAuthority);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentAuthorityDTO toDtoId(DepartmentAuthority departmentAuthority);
}
