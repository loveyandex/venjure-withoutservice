package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.JobRecord;
import com.venjure.repository.JobRecordRepository;
import com.venjure.service.criteria.JobRecordCriteria;
import com.venjure.service.dto.JobRecordDTO;
import com.venjure.service.mapper.JobRecordMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link JobRecord} entities in the database.
 * The main input is a {@link JobRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobRecordDTO} or a {@link Page} of {@link JobRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobRecordQueryService extends QueryService<JobRecord> {

    private final Logger log = LoggerFactory.getLogger(JobRecordQueryService.class);

    private final JobRecordRepository jobRecordRepository;

    private final JobRecordMapper jobRecordMapper;

    public JobRecordQueryService(JobRecordRepository jobRecordRepository, JobRecordMapper jobRecordMapper) {
        this.jobRecordRepository = jobRecordRepository;
        this.jobRecordMapper = jobRecordMapper;
    }

    /**
     * Return a {@link List} of {@link JobRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobRecordDTO> findByCriteria(JobRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobRecord> specification = createSpecification(criteria);
        return jobRecordMapper.toDto(jobRecordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JobRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobRecordDTO> findByCriteria(JobRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobRecord> specification = createSpecification(criteria);
        return jobRecordRepository.findAll(specification, page).map(jobRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobRecord> specification = createSpecification(criteria);
        return jobRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link JobRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobRecord> createSpecification(JobRecordCriteria criteria) {
        Specification<JobRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobRecord_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), JobRecord_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), JobRecord_.updatedat));
            }
            if (criteria.getQueuename() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQueuename(), JobRecord_.queuename));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildStringSpecification(criteria.getData(), JobRecord_.data));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), JobRecord_.state));
            }
            if (criteria.getProgress() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProgress(), JobRecord_.progress));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResult(), JobRecord_.result));
            }
            if (criteria.getError() != null) {
                specification = specification.and(buildStringSpecification(criteria.getError(), JobRecord_.error));
            }
            if (criteria.getStartedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartedat(), JobRecord_.startedat));
            }
            if (criteria.getSettledat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSettledat(), JobRecord_.settledat));
            }
            if (criteria.getIssettled() != null) {
                specification = specification.and(buildSpecification(criteria.getIssettled(), JobRecord_.issettled));
            }
            if (criteria.getRetries() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRetries(), JobRecord_.retries));
            }
            if (criteria.getAttempts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAttempts(), JobRecord_.attempts));
            }
        }
        return specification;
    }
}
