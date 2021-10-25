package com.venjure.repository;

import com.venjure.domain.Facet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Facet entity.
 */
@Repository
public interface FacetRepository extends JpaRepository<Facet, Long>, JpaSpecificationExecutor<Facet> {
    @Query(
        value = "select distinct facet from Facet facet left join fetch facet.channels",
        countQuery = "select count(distinct facet) from Facet facet"
    )
    Page<Facet> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct facet from Facet facet left join fetch facet.channels")
    List<Facet> findAllWithEagerRelationships();

    @Query("select facet from Facet facet left join fetch facet.channels where facet.id =:id")
    Optional<Facet> findOneWithEagerRelationships(@Param("id") Long id);
}
