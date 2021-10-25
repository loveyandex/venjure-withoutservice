package com.venjure.service.mapper;

import com.venjure.domain.*;
import com.venjure.service.dto.JobRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobRecord} and its DTO {@link JobRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobRecordMapper extends EntityMapper<JobRecordDTO, JobRecord> {}
