package com.venjure.service;

import com.venjure.domain.JobRecord;
import com.venjure.repository.JobRecordRepository;
import com.venjure.service.dto.JobRecordDTO;
import com.venjure.service.mapper.JobRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JobRecord}.
 */
@Service
@Transactional
public class JobRecordService {

    private final Logger log = LoggerFactory.getLogger(JobRecordService.class);

    private final JobRecordRepository jobRecordRepository;

    private final JobRecordMapper jobRecordMapper;

    public JobRecordService(JobRecordRepository jobRecordRepository, JobRecordMapper jobRecordMapper) {
        this.jobRecordRepository = jobRecordRepository;
        this.jobRecordMapper = jobRecordMapper;
    }

    /**
     * Save a jobRecord.
     *
     * @param jobRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public JobRecordDTO save(JobRecordDTO jobRecordDTO) {
        log.debug("Request to save JobRecord : {}", jobRecordDTO);
        JobRecord jobRecord = jobRecordMapper.toEntity(jobRecordDTO);
        jobRecord = jobRecordRepository.save(jobRecord);
        return jobRecordMapper.toDto(jobRecord);
    }

    /**
     * Partially update a jobRecord.
     *
     * @param jobRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobRecordDTO> partialUpdate(JobRecordDTO jobRecordDTO) {
        log.debug("Request to partially update JobRecord : {}", jobRecordDTO);

        return jobRecordRepository
            .findById(jobRecordDTO.getId())
            .map(
                existingJobRecord -> {
                    jobRecordMapper.partialUpdate(existingJobRecord, jobRecordDTO);
                    return existingJobRecord;
                }
            )
            .map(jobRecordRepository::save)
            .map(jobRecordMapper::toDto);
    }

    /**
     * Get all the jobRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobRecords");
        return jobRecordRepository.findAll(pageable).map(jobRecordMapper::toDto);
    }

    /**
     * Get one jobRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobRecordDTO> findOne(Long id) {
        log.debug("Request to get JobRecord : {}", id);
        return jobRecordRepository.findById(id).map(jobRecordMapper::toDto);
    }

    /**
     * Delete the jobRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JobRecord : {}", id);
        jobRecordRepository.deleteById(id);
    }
}
