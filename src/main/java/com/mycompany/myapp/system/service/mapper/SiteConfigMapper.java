package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.*;
import com.mycompany.myapp.system.service.dto.SiteConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SiteConfig} and its DTO {@link SiteConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SiteConfigMapper extends EntityMapper<SiteConfigDTO, SiteConfig> {}
