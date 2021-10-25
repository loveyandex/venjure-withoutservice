package com.venjure.service;

import com.venjure.domain.Administrator;
import com.venjure.repository.AdministratorRepository;
import com.venjure.service.dto.AdministratorDTO;
import com.venjure.service.mapper.AdministratorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Administrator}.
 */
@Service
@Transactional
public class AdministratorService {

    private final Logger log = LoggerFactory.getLogger(AdministratorService.class);

    private final AdministratorRepository administratorRepository;

    private final AdministratorMapper administratorMapper;

    public AdministratorService(AdministratorRepository administratorRepository, AdministratorMapper administratorMapper) {
        this.administratorRepository = administratorRepository;
        this.administratorMapper = administratorMapper;
    }

    /**
     * Save a administrator.
     *
     * @param administratorDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministratorDTO save(AdministratorDTO administratorDTO) {
        log.debug("Request to save Administrator : {}", administratorDTO);
        Administrator administrator = administratorMapper.toEntity(administratorDTO);
        administrator = administratorRepository.save(administrator);
        return administratorMapper.toDto(administrator);
    }

    /**
     * Partially update a administrator.
     *
     * @param administratorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministratorDTO> partialUpdate(AdministratorDTO administratorDTO) {
        log.debug("Request to partially update Administrator : {}", administratorDTO);

        return administratorRepository
            .findById(administratorDTO.getId())
            .map(
                existingAdministrator -> {
                    administratorMapper.partialUpdate(existingAdministrator, administratorDTO);
                    return existingAdministrator;
                }
            )
            .map(administratorRepository::save)
            .map(administratorMapper::toDto);
    }

    /**
     * Get all the administrators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdministratorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Administrators");
        return administratorRepository.findAll(pageable).map(administratorMapper::toDto);
    }

    /**
     * Get one administrator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministratorDTO> findOne(Long id) {
        log.debug("Request to get Administrator : {}", id);
        return administratorRepository.findById(id).map(administratorMapper::toDto);
    }

    /**
     * Delete the administrator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Administrator : {}", id);
        administratorRepository.deleteById(id);
    }
}
