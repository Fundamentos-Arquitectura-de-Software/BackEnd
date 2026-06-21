package com.acme.backendfreshsense.billing.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.billing.domain.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE SubscriptionEntity s SET s.status = :cancelled WHERE s.userId = :userId AND s.status = :active")
    void cancelActiveByUserId(@Param("userId") Long userId,
                              @Param("active") SubscriptionStatus active,
                              @Param("cancelled") SubscriptionStatus cancelled);
}
