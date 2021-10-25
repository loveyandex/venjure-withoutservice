package com.venjure.service;

import com.venjure.domain.GlobalSettings;
import com.venjure.repository.GlobalSettingsRepository;
import com.venjure.service.dto.GlobalSettingsDTO;
import com.venjure.service.mapper.GlobalSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GlobalSettings}.
 */
@Service
@Transactional
public class GlobalSettingsService {

    private final Logger log = LoggerFactory.getLogger(GlobalSettingsService.class);

    private final GlobalSettingsRepository globalSettingsRepository;

    private final GlobalSettingsMapper globalSettingsMapper;

    public GlobalSettingsService(GlobalSettingsRepository globalSettingsRepository, GlobalSettingsMapper globalSettingsMapper) {
        this.globalSettingsRepository = globalSettingsRepository;
        this.globalSettingsMapper = globalSettingsMapper;
    }

    /**
     * Save a globalSettings.
     *
     * @param globalSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public GlobalSettingsDTO save(GlobalSettingsDTO globalSettingsDTO) {
        log.debug("Request to save GlobalSettings : {}", globalSettingsDTO);
        GlobalSettings globalSettings = globalSettingsMapper.toEntity(globalSettingsDTO);
        globalSettings = globalSettingsRepository.save(globalSettings);
        return globalSettingsMapper.toDto(globalSettings);
    }

    /**
     * Partially update a globalSettings.
     *
     * @param globalSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GlobalSettingsDTO> partialUpdate(GlobalSettingsDTO globalSettingsDTO) {
        log.debug("Request to partially update GlobalSettings : {}", globalSettingsDTO);

        return globalSettingsRepository
            .findById(globalSettingsDTO.getId())
            .map(
                existingGlobalSettings -> {
                    globalSettingsMapper.partialUpdate(existingGlobalSettings, globalSettingsDTO);
                    return existingGlobalSettings;
                }
            )
            .map(globalSettingsRepository::save)
            .map(globalSettingsMapper::toDto);
    }

    /**
     * Get all the globalSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GlobalSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GlobalSettings");
        return globalSettingsRepository.findAll(pageable).map(globalSettingsMapper::toDto);
    }

    /**
     * Get one globalSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GlobalSettingsDTO> findOne(Long id) {
        log.debug("Request to get GlobalSettings : {}", id);
        return globalSettingsRepository.findById(id).map(globalSettingsMapper::toDto);
    }

    /**
     * Delete the globalSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GlobalSettings : {}", id);
        globalSettingsRepository.deleteById(id);
    }
}
