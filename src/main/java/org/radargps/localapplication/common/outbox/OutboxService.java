package org.radargps.localapplication.common.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxRepository outboxRepository;
    public OutboxService(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;}

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOutbox(Outbox outbox) {
        outboxRepository.save(outbox);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteOutBoxById(UUID outboxId) {
        outboxRepository.deleteById(outboxId);
    }

    public List<Outbox> fetchPendingMessages() {
        return outboxRepository.findAllByCreateAtBefore(ZonedDateTime.now());
    }
}
