package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.UReportFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UReportFile} and its DTO {@link UReportFileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UReportFileMapper extends EntityMapper<UReportFileDTO, UReportFile> {}
