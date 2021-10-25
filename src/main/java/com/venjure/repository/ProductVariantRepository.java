package com.venjure.repository;

import com.venjure.domain.ProductVariant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductVariant entity.
 */
@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {
    @Query(
        value = "select distinct productVariant from ProductVariant productVariant left join fetch productVariant.channels left join fetch productVariant.productVariants left join fetch productVariant.facetValues left join fetch productVariant.productOptions",
        countQuery = "select count(distinct productVariant) from ProductVariant productVariant"
    )
    Page<ProductVariant> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct productVariant from ProductVariant productVariant left join fetch productVariant.channels left join fetch productVariant.productVariants left join fetch productVariant.facetValues left join fetch productVariant.productOptions"
    )
    List<ProductVariant> findAllWithEagerRelationships();

    @Query(
        "select productVariant from ProductVariant productVariant left join fetch productVariant.channels left join fetch productVariant.productVariants left join fetch productVariant.facetValues left join fetch productVariant.productOptions where productVariant.id =:id"
    )
    Optional<ProductVariant> findOneWithEagerRelationships(@Param("id") Long id);
}
