package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.OssConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OssConfig} and its DTO {@link OssConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OssConfigMapper extends EntityMapper<OssConfigDTO, OssConfig> {}
