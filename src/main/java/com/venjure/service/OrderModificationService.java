package com.venjure.service;

import com.venjure.domain.OrderModification;
import com.venjure.repository.OrderModificationRepository;
import com.venjure.service.dto.OrderModificationDTO;
import com.venjure.service.mapper.OrderModificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderModification}.
 */
@Service
@Transactional
public class OrderModificationService {

    private final Logger log = LoggerFactory.getLogger(OrderModificationService.class);

    private final OrderModificationRepository orderModificationRepository;

    private final OrderModificationMapper orderModificationMapper;

    public OrderModificationService(
        OrderModificationRepository orderModificationRepository,
        OrderModificationMapper orderModificationMapper
    ) {
        this.orderModificationRepository = orderModificationRepository;
        this.orderModificationMapper = orderModificationMapper;
    }

    /**
     * Save a orderModification.
     *
     * @param orderModificationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderModificationDTO save(OrderModificationDTO orderModificationDTO) {
        log.debug("Request to save OrderModification : {}", orderModificationDTO);
        OrderModification orderModification = orderModificationMapper.toEntity(orderModificationDTO);
        orderModification = orderModificationRepository.save(orderModification);
        return orderModificationMapper.toDto(orderModification);
    }

    /**
     * Partially update a orderModification.
     *
     * @param orderModificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderModificationDTO> partialUpdate(OrderModificationDTO orderModificationDTO) {
        log.debug("Request to partially update OrderModification : {}", orderModificationDTO);

        return orderModificationRepository
            .findById(orderModificationDTO.getId())
            .map(
                existingOrderModification -> {
                    orderModificationMapper.partialUpdate(existingOrderModification, orderModificationDTO);
                    return existingOrderModification;
                }
            )
            .map(orderModificationRepository::save)
            .map(orderModificationMapper::toDto);
    }

    /**
     * Get all the orderModifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderModificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderModifications");
        return orderModificationRepository.findAll(pageable).map(orderModificationMapper::toDto);
    }

    /**
     * Get one orderModification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderModificationDTO> findOne(Long id) {
        log.debug("Request to get OrderModification : {}", id);
        return orderModificationRepository.findById(id).map(orderModificationMapper::toDto);
    }

    /**
     * Delete the orderModification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderModification : {}", id);
        orderModificationRepository.deleteById(id);
    }
}
