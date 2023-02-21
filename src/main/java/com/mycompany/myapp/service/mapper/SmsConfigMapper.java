package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SmsConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsConfig} and its DTO {@link SmsConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsConfigMapper extends EntityMapper<SmsConfigDTO, SmsConfig> {}
