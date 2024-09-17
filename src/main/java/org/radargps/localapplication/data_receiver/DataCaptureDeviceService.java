package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.common.Cache;
import org.radargps.localapplication.common.outbox.OutboxService;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceCreateCommand;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceRequest;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceUpdateCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataCaptureDeviceService {
    private final DataCaptureDeviceRepository dataCaptureDeviceRepository;
    private final DataCaptureDeviceMapper dataCaptureDeviceMapper;
    private final DataService dataService;
    private final OutboxService outboxService;

    public DataCaptureDeviceService(DataCaptureDeviceRepository dataCaptureDeviceRepository, DataCaptureDeviceMapper dataCaptureDeviceMapper,
                                    DataService dataService, OutboxService outboxService) {
        this.dataCaptureDeviceRepository = dataCaptureDeviceRepository;
        this.dataCaptureDeviceMapper = dataCaptureDeviceMapper;
        this.dataService = dataService;
        this.outboxService = outboxService;
    }

    @Transactional
    public DataCaptureDeviceRequest create(DataCaptureDeviceCreateCommand createCommand) {
        var device = dataCaptureDeviceMapper.toEntity(createCommand);
        device = dataCaptureDeviceRepository.save(device);
        return dataCaptureDeviceMapper.toRequest(device);
    }


    @Transactional
    public DataCaptureDeviceRequest partialUpdate(UUID dataCaptureDeviceId, DataCaptureDeviceUpdateCommand command) {
        var device = dataCaptureDeviceRepository.findById(dataCaptureDeviceId)
                        .orElseThrow(RuntimeException::new);
        dataCaptureDeviceMapper.partialUpdate(device, command);
        Cache.evictDevice(dataCaptureDeviceId);
        dataCaptureDeviceRepository.save(device);
        return dataCaptureDeviceMapper.toRequest(device);
    }

    @Transactional
    public Optional<DataCaptureDeviceRequest> findOne(UUID deviceId) {
        return findOneExec(deviceId).map(dataCaptureDeviceMapper::toRequest);
    }

    public Optional<DataCaptureDevice> findOneExec(UUID deviceId) {
        Optional<DataCaptureDevice> result = Optional.ofNullable(Cache.deviceIdToDataCaptureDeviceGet(deviceId));
        if (result.isEmpty()) {
            result = dataCaptureDeviceRepository.findById(deviceId);
            result.ifPresent((device -> Cache.deviceIdToDataCaptureDevicePut(deviceId, device)));
        }
        return result;
    }

    @Transactional
    public void updateLatestDeviceData(UUID deviceId, Data data) {
        dataCaptureDeviceRepository.setLastDataIdAndLastDataTimeByDeviceId(deviceId, data.getId(), data.getServerTime());
        Cache.deviceIdToLastDataPut(deviceId, data);
    }

    @Transactional
    public Optional<Data> findLatestDeviceDataExec(UUID deviceId) {
        Optional<Data> result = Optional.ofNullable(Cache.deviceIdToLastDataGet(deviceId));
        if (result.isEmpty()) {
            result = dataCaptureDeviceRepository.findLatestDeviceData(deviceId);
            result.ifPresent((data -> Cache.deviceIdToLastDataPut(deviceId, data)));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DataCaptureDeviceRequest> findByUniqueId(String uniqueId) {
        return findByUniqueIdExec(uniqueId).map(dataCaptureDeviceMapper::toRequest);
    }

    public Optional<DataCaptureDevice> findByUniqueIdExec(String uniqueId) {
        Optional<DataCaptureDevice> result;
        if (Cache.uniqueIdToDataCaptureDeviceContainsKey(uniqueId)) {
            result = Optional.of(Cache.uniqueIdToDataCaptureDeviceGet(uniqueId));
        } else {
            result = dataCaptureDeviceRepository.findByUniqueId(uniqueId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Page<DataCaptureDeviceRequest> findByCompany(UUID companyId) {
        Pageable pageable = PageRequest.of(0, 100);
        var result = dataCaptureDeviceRepository.findByCompanyId(companyId, pageable);
        return new Page<>(result.getContent().stream().map(dataCaptureDeviceMapper::toRequest).toList(),
                result.getTotalElements());
    }
}
