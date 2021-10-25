package com.venjure.repository;

import com.venjure.domain.Jorder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Jorder entity.
 */
@Repository
public interface JorderRepository extends JpaRepository<Jorder, Long>, JpaSpecificationExecutor<Jorder> {
    @Query(
        value = "select distinct jorder from Jorder jorder left join fetch jorder.channels left join fetch jorder.promotions",
        countQuery = "select count(distinct jorder) from Jorder jorder"
    )
    Page<Jorder> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct jorder from Jorder jorder left join fetch jorder.channels left join fetch jorder.promotions")
    List<Jorder> findAllWithEagerRelationships();

    @Query("select jorder from Jorder jorder left join fetch jorder.channels left join fetch jorder.promotions where jorder.id =:id")
    Optional<Jorder> findOneWithEagerRelationships(@Param("id") Long id);
}
