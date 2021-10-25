package com.venjure.service;

import com.venjure.domain.OrderLine;
import com.venjure.repository.OrderLineRepository;
import com.venjure.service.dto.OrderLineDTO;
import com.venjure.service.mapper.OrderLineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderLine}.
 */
@Service
@Transactional
public class OrderLineService {

    private final Logger log = LoggerFactory.getLogger(OrderLineService.class);

    private final OrderLineRepository orderLineRepository;

    private final OrderLineMapper orderLineMapper;

    public OrderLineService(OrderLineRepository orderLineRepository, OrderLineMapper orderLineMapper) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
    }

    /**
     * Save a orderLine.
     *
     * @param orderLineDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderLineDTO save(OrderLineDTO orderLineDTO) {
        log.debug("Request to save OrderLine : {}", orderLineDTO);
        OrderLine orderLine = orderLineMapper.toEntity(orderLineDTO);
        orderLine = orderLineRepository.save(orderLine);
        return orderLineMapper.toDto(orderLine);
    }

    /**
     * Partially update a orderLine.
     *
     * @param orderLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderLineDTO> partialUpdate(OrderLineDTO orderLineDTO) {
        log.debug("Request to partially update OrderLine : {}", orderLineDTO);

        return orderLineRepository
            .findById(orderLineDTO.getId())
            .map(
                existingOrderLine -> {
                    orderLineMapper.partialUpdate(existingOrderLine, orderLineDTO);
                    return existingOrderLine;
                }
            )
            .map(orderLineRepository::save)
            .map(orderLineMapper::toDto);
    }

    /**
     * Get all the orderLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderLines");
        return orderLineRepository.findAll(pageable).map(orderLineMapper::toDto);
    }

    /**
     * Get one orderLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderLineDTO> findOne(Long id) {
        log.debug("Request to get OrderLine : {}", id);
        return orderLineRepository.findById(id).map(orderLineMapper::toDto);
    }

    /**
     * Delete the orderLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderLine : {}", id);
        orderLineRepository.deleteById(id);
    }
}
