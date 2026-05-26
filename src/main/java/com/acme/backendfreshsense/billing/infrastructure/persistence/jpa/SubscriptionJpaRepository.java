package com.acme.backendfreshsense.billing.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.billing.domain.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

    Optional<SubscriptionEntity> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    @Modifying
    @Query("UPDATE SubscriptionEntity s SET s.status = 'CANCELLED' WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    void cancelActiveByUserId(@Param("userId") Long userId);
}
