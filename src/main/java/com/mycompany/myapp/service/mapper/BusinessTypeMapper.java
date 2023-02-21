package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.BusinessTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessType} and its DTO {@link BusinessTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessTypeMapper extends EntityMapper<BusinessTypeDTO, BusinessType> {}
