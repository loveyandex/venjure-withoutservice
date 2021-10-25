package com.venjure.service;

import com.venjure.domain.*; // for static metamodels
import com.venjure.domain.Channel;
import com.venjure.repository.ChannelRepository;
import com.venjure.service.criteria.ChannelCriteria;
import com.venjure.service.dto.ChannelDTO;
import com.venjure.service.mapper.ChannelMapper;
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
 * Service for executing complex queries for {@link Channel} entities in the database.
 * The main input is a {@link ChannelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChannelDTO} or a {@link Page} of {@link ChannelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChannelQueryService extends QueryService<Channel> {

    private final Logger log = LoggerFactory.getLogger(ChannelQueryService.class);

    private final ChannelRepository channelRepository;

    private final ChannelMapper channelMapper;

    public ChannelQueryService(ChannelRepository channelRepository, ChannelMapper channelMapper) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
    }

    /**
     * Return a {@link List} of {@link ChannelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChannelDTO> findByCriteria(ChannelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Channel> specification = createSpecification(criteria);
        return channelMapper.toDto(channelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChannelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChannelDTO> findByCriteria(ChannelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Channel> specification = createSpecification(criteria);
        return channelRepository.findAll(specification, page).map(channelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChannelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Channel> specification = createSpecification(criteria);
        return channelRepository.count(specification);
    }

    /**
     * Function to convert {@link ChannelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Channel> createSpecification(ChannelCriteria criteria) {
        Specification<Channel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Channel_.id));
            }
            if (criteria.getCreatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedat(), Channel_.createdat));
            }
            if (criteria.getUpdatedat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedat(), Channel_.updatedat));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Channel_.code));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), Channel_.token));
            }
            if (criteria.getDefaultlanguagecode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDefaultlanguagecode(), Channel_.defaultlanguagecode));
            }
            if (criteria.getCurrencycode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencycode(), Channel_.currencycode));
            }
            if (criteria.getPricesincludetax() != null) {
                specification = specification.and(buildSpecification(criteria.getPricesincludetax(), Channel_.pricesincludetax));
            }
            if (criteria.getDefaulttaxzoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDefaulttaxzoneId(),
                            root -> root.join(Channel_.defaulttaxzone, JoinType.LEFT).get(Zone_.id)
                        )
                    );
            }
            if (criteria.getDefaultshippingzoneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDefaultshippingzoneId(),
                            root -> root.join(Channel_.defaultshippingzone, JoinType.LEFT).get(Zone_.id)
                        )
                    );
            }
            if (criteria.getPaymentMethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentMethodId(),
                            root -> root.join(Channel_.paymentMethods, JoinType.LEFT).get(PaymentMethod_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(Channel_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getPromotionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPromotionId(),
                            root -> root.join(Channel_.promotions, JoinType.LEFT).get(Promotion_.id)
                        )
                    );
            }
            if (criteria.getShippingMethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShippingMethodId(),
                            root -> root.join(Channel_.shippingMethods, JoinType.LEFT).get(ShippingMethod_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Channel_.customers, JoinType.LEFT).get(Customer_.id))
                    );
            }
            if (criteria.getFacetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFacetId(), root -> root.join(Channel_.facets, JoinType.LEFT).get(Facet_.id))
                    );
            }
            if (criteria.getFacetValueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFacetValueId(),
                            root -> root.join(Channel_.facetValues, JoinType.LEFT).get(FacetValue_.id)
                        )
                    );
            }
            if (criteria.getJorderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getJorderId(), root -> root.join(Channel_.jorders, JoinType.LEFT).get(Jorder_.id))
                    );
            }
            if (criteria.getProductVariantId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductVariantId(),
                            root -> root.join(Channel_.productVariants, JoinType.LEFT).get(ProductVariant_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
