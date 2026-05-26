package com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InAppNotificationJpaRepository extends JpaRepository<InAppNotificationEntity, Long> {

    List<InAppNotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query("UPDATE InAppNotificationEntity n SET n.read = true WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);
}
