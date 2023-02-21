package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.DataPermissionRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DataPermissionRule} and its DTO {@link DataPermissionRuleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataPermissionRuleMapper extends EntityMapper<DataPermissionRuleDTO, DataPermissionRule> {}
