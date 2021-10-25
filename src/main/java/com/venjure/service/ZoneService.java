package com.venjure.service;

import com.venjure.domain.Zone;
import com.venjure.repository.ZoneRepository;
import com.venjure.service.dto.ZoneDTO;
import com.venjure.service.mapper.ZoneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Zone}.
 */
@Service
@Transactional
public class ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneService.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    public ZoneService(ZoneRepository zoneRepository, ZoneMapper zoneMapper) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    /**
     * Save a zone.
     *
     * @param zoneDTO the entity to save.
     * @return the persisted entity.
     */
    public ZoneDTO save(ZoneDTO zoneDTO) {
        log.debug("Request to save Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        return zoneMapper.toDto(zone);
    }

    /**
     * Partially update a zone.
     *
     * @param zoneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ZoneDTO> partialUpdate(ZoneDTO zoneDTO) {
        log.debug("Request to partially update Zone : {}", zoneDTO);

        return zoneRepository
            .findById(zoneDTO.getId())
            .map(
                existingZone -> {
                    zoneMapper.partialUpdate(existingZone, zoneDTO);
                    return existingZone;
                }
            )
            .map(zoneRepository::save)
            .map(zoneMapper::toDto);
    }

    /**
     * Get all the zones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ZoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll(pageable).map(zoneMapper::toDto);
    }

    /**
     * Get all the zones with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ZoneDTO> findAllWithEagerRelationships(Pageable pageable) {
        return zoneRepository.findAllWithEagerRelationships(pageable).map(zoneMapper::toDto);
    }

    /**
     * Get one zone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ZoneDTO> findOne(Long id) {
        log.debug("Request to get Zone : {}", id);
        return zoneRepository.findOneWithEagerRelationships(id).map(zoneMapper::toDto);
    }

    /**
     * Delete the zone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.deleteById(id);
    }
}
