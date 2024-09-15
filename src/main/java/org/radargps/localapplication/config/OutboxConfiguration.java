package org.radargps.localapplication.config;

import org.radargps.localapplication.common.outbox.OutboxService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableScheduling
public class OutboxConfiguration {
//    private final OutboxService outboxService;
//    private final GeofenceMessagePublisher geofenceMessagePublisher;
//    private final TransactionTemplate transactionTemplate;
//
//    public OutboxConfiguration(OutboxService outboxService,
//                               GeofenceMessagePublisher geofenceMessagePublisher,
//                               TransactionTemplate transactionTemplate) {
//        this.outboxService = outboxService;
//        this.geofenceMessagePublisher = geofenceMessagePublisher;
//        this.transactionTemplate = transactionTemplate;
//    }
//
//    @Scheduled(fixedRate = 20000) // Runs every 1 seconds
//    public void processGeofenceOutbox() {
//        outboxService.fetchPendingMessages()
//                .forEach(outbox -> {
//                    switch (outbox.getType()) {
//                        case OutboxType.GEOFENCE -> publishAndDeleteOutbox(outbox, geofenceMessagePublisher);
//                    }
//                });
//    }
//
//    public void publishAndDeleteOutbox(Outbox outbox, EventPublisher publisher) {
//        transactionTemplate.executeWithoutResult(transactionStatus -> {
//            publisher.publish(outbox.getEvent());
//            outboxService.deleteOutBoxById(outbox.getId());
//        });
//    }
}
