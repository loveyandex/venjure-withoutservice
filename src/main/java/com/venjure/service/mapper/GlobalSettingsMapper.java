package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.GlobalSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GlobalSettings} and its DTO {@link GlobalSettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GlobalSettingsMapper extends EntityMapper<GlobalSettingsDTO, GlobalSettings> {}
