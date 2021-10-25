package com.venjure.repository;

import com.venjure.domain.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderItem entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    @Query(
        value = "select distinct orderItem from OrderItem orderItem left join fetch orderItem.fulfillments left join fetch orderItem.orderModifications",
        countQuery = "select count(distinct orderItem) from OrderItem orderItem"
    )
    Page<OrderItem> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct orderItem from OrderItem orderItem left join fetch orderItem.fulfillments left join fetch orderItem.orderModifications"
    )
    List<OrderItem> findAllWithEagerRelationships();

    @Query(
        "select orderItem from OrderItem orderItem left join fetch orderItem.fulfillments left join fetch orderItem.orderModifications where orderItem.id =:id"
    )
    Optional<OrderItem> findOneWithEagerRelationships(@Param("id") Long id);
}
