package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.ExampleEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExampleEntity} and its DTO {@link ExampleEntityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExampleEntityMapper extends EntityMapper<ExampleEntityDTO, ExampleEntity> {}
