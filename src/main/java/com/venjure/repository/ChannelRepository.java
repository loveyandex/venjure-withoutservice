package com.venjure.repository;

import com.venjure.domain.Channel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Channel entity.
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>, JpaSpecificationExecutor<Channel> {
    @Query(
        value = "select distinct channel from Channel channel left join fetch channel.paymentMethods left join fetch channel.products left join fetch channel.promotions left join fetch channel.shippingMethods",
        countQuery = "select count(distinct channel) from Channel channel"
    )
    Page<Channel> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct channel from Channel channel left join fetch channel.paymentMethods left join fetch channel.products left join fetch channel.promotions left join fetch channel.shippingMethods"
    )
    List<Channel> findAllWithEagerRelationships();

    @Query(
        "select channel from Channel channel left join fetch channel.paymentMethods left join fetch channel.products left join fetch channel.promotions left join fetch channel.shippingMethods where channel.id =:id"
    )
    Optional<Channel> findOneWithEagerRelationships(@Param("id") Long id);
}
