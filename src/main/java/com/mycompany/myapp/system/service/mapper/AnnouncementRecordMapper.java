package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnouncementRecord} and its DTO {@link AnnouncementRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnouncementRecordMapper extends EntityMapper<AnnouncementRecordDTO, AnnouncementRecord> {}
