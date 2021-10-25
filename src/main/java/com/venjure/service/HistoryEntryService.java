package com.venjure.service;

import com.venjure.domain.HistoryEntry;
import com.venjure.repository.HistoryEntryRepository;
import com.venjure.service.dto.HistoryEntryDTO;
import com.venjure.service.mapper.HistoryEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoryEntry}.
 */
@Service
@Transactional
public class HistoryEntryService {

    private final Logger log = LoggerFactory.getLogger(HistoryEntryService.class);

    private final HistoryEntryRepository historyEntryRepository;

    private final HistoryEntryMapper historyEntryMapper;

    public HistoryEntryService(HistoryEntryRepository historyEntryRepository, HistoryEntryMapper historyEntryMapper) {
        this.historyEntryRepository = historyEntryRepository;
        this.historyEntryMapper = historyEntryMapper;
    }

    /**
     * Save a historyEntry.
     *
     * @param historyEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoryEntryDTO save(HistoryEntryDTO historyEntryDTO) {
        log.debug("Request to save HistoryEntry : {}", historyEntryDTO);
        HistoryEntry historyEntry = historyEntryMapper.toEntity(historyEntryDTO);
        historyEntry = historyEntryRepository.save(historyEntry);
        return historyEntryMapper.toDto(historyEntry);
    }

    /**
     * Partially update a historyEntry.
     *
     * @param historyEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoryEntryDTO> partialUpdate(HistoryEntryDTO historyEntryDTO) {
        log.debug("Request to partially update HistoryEntry : {}", historyEntryDTO);

        return historyEntryRepository
            .findById(historyEntryDTO.getId())
            .map(
                existingHistoryEntry -> {
                    historyEntryMapper.partialUpdate(existingHistoryEntry, historyEntryDTO);
                    return existingHistoryEntry;
                }
            )
            .map(historyEntryRepository::save)
            .map(historyEntryMapper::toDto);
    }

    /**
     * Get all the historyEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoryEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoryEntries");
        return historyEntryRepository.findAll(pageable).map(historyEntryMapper::toDto);
    }

    /**
     * Get one historyEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoryEntryDTO> findOne(Long id) {
        log.debug("Request to get HistoryEntry : {}", id);
        return historyEntryRepository.findById(id).map(historyEntryMapper::toDto);
    }

    /**
     * Delete the historyEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoryEntry : {}", id);
        historyEntryRepository.deleteById(id);
    }
}
