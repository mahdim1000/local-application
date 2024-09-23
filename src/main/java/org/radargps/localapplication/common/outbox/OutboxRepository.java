package org.radargps.localapplication.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    List<Outbox> findAllByCreateAtBefore(ZonedDateTime now);
}
