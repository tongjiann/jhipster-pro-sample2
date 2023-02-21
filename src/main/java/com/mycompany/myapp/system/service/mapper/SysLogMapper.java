package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.SysLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SysLog} and its DTO {@link SysLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SysLogMapper extends EntityMapper<SysLogDTO, SysLog> {}
