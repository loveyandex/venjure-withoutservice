package com.venjure.repository;

import com.venjure.domain.FacetValue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacetValue entity.
 */
@Repository
public interface FacetValueRepository extends JpaRepository<FacetValue, Long>, JpaSpecificationExecutor<FacetValue> {
    @Query(
        value = "select distinct facetValue from FacetValue facetValue left join fetch facetValue.channels left join fetch facetValue.products",
        countQuery = "select count(distinct facetValue) from FacetValue facetValue"
    )
    Page<FacetValue> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct facetValue from FacetValue facetValue left join fetch facetValue.channels left join fetch facetValue.products")
    List<FacetValue> findAllWithEagerRelationships();

    @Query(
        "select facetValue from FacetValue facetValue left join fetch facetValue.channels left join fetch facetValue.products where facetValue.id =:id"
    )
    Optional<FacetValue> findOneWithEagerRelationships(@Param("id") Long id);
}
