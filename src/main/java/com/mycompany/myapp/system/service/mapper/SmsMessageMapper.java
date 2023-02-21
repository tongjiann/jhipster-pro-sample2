package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.SmsMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmsMessage} and its DTO {@link SmsMessageDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsMessageMapper extends EntityMapper<SmsMessageDTO, SmsMessage> {}
