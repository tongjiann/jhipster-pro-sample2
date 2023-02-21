package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.AnnouncementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Announcement} and its DTO {@link AnnouncementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnouncementMapper extends EntityMapper<AnnouncementDTO, Announcement> {}
