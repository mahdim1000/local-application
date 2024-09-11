package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.common.Cache;
import org.radargps.localapplication.common.outbox.DomainEvent;
import org.radargps.localapplication.common.outbox.Outbox;
import org.radargps.localapplication.common.outbox.OutboxService;
import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.data_receiver.event.ProductPalletAssigned;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataCaptureDeviceService {
    private final DataCaptureDeviceRepository dataCaptureDeviceRepository;
    private final DataService dataService;
    private final OutboxService outboxService;

    public DataCaptureDeviceService(DataCaptureDeviceRepository dataCaptureDeviceRepository,
                                    DataService dataService, OutboxService outboxService) {
        this.dataCaptureDeviceRepository = dataCaptureDeviceRepository;
        this.dataService = dataService;
        this.outboxService = outboxService;
    }

    @Transactional
    public DataCaptureDevice save(DataCaptureDevice dataCaptureDevice) {
        return dataCaptureDeviceRepository.save(dataCaptureDevice);
    }

    public Optional<DataCaptureDevice> findById(UUID deviceId) {
        Optional<DataCaptureDevice> result;
        if (Cache.containsKeyDeviceIdToDataCaptureDevice(deviceId)) {
            result = Optional.of(Cache.getDeviceIdToDataCaptureDevice(deviceId));
        } else {
            result = dataCaptureDeviceRepository.findById(deviceId);
        }

        return result;
    }

    @Transactional
    public Optional<Data> getLatestData(UUID deviceId) {
        Optional<Data> result;
        if (Cache.containsKeyDeviceIdToLastData(deviceId)) {
            result = Optional.of(Cache.getDeviceIdToLastData(deviceId));
        } else {
            result = dataCaptureDeviceRepository.findById(deviceId)
                    .filter(device -> device.getLastDataId() != null)
                    .map(device -> dataService.findById(device.getLastDataId())).get();
        }
        return result;
    }

    @Transactional
    public void updateLatestDeviceDataAndPublish(UUID deviceId, Data data) {
        dataCaptureDeviceRepository.setLastDataIdAndLastDataTimeByDeviceId(deviceId, data.getId(), data.getLastServerTime());
        Cache.putDeviceIdToLastData(deviceId, data);
        DomainEvent event = new ProductPalletAssigned()
        outboxService.saveOutbox(new Outbox());
    }

    public Optional<DataCaptureDevice> findByUniqueId(String uniqueId) {
        Optional<DataCaptureDevice> result;
        if (Cache.containsKeyUniqueIdToDataCaptureDevice(uniqueId)) {
            result = Optional.of(Cache.getUniqueIdToDataCaptureDevice(uniqueId));
        } else {
            result = dataCaptureDeviceRepository.findByUniqueId(uniqueId);
        }
        return result;
    }
}
