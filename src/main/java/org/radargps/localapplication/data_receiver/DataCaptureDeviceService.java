package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.common.Cache;
import org.radargps.localapplication.common.outbox.OutboxService;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceCreateCommand;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceRequest;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceUpdateCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataCaptureDeviceService {
    private final DataCaptureDeviceMapper dataCaptureDeviceMapper;
    private final DataCaptureDeviceInternalService dataCaptureDeviceInternalService;

    public DataCaptureDeviceService(DataCaptureDeviceMapper dataCaptureDeviceMapper,
                                    DataCaptureDeviceInternalService dataCaptureDeviceInternalService) {
        this.dataCaptureDeviceMapper = dataCaptureDeviceMapper;
        this.dataCaptureDeviceInternalService = dataCaptureDeviceInternalService;
    }

    @Transactional
    public DataCaptureDeviceRequest create(DataCaptureDeviceCreateCommand createCommand) {
        var device = dataCaptureDeviceMapper.toEntity(createCommand);
        device = dataCaptureDeviceInternalService.create(device);
        return dataCaptureDeviceMapper.toRequest(device);
    }


    @Transactional
    public DataCaptureDeviceRequest partialUpdate(UUID dataCaptureDeviceId, DataCaptureDeviceUpdateCommand command) {
        var device = dataCaptureDeviceInternalService.findOne(dataCaptureDeviceId)
                .orElseThrow(RuntimeException::new);

        dataCaptureDeviceMapper.partialUpdate(device, command);
        dataCaptureDeviceInternalService.updateDevice(device);
        Cache.evictDevice(dataCaptureDeviceId);
        return dataCaptureDeviceMapper.toRequest(device);
    }

    @Transactional
    public Optional<DataCaptureDeviceRequest> findOne(UUID deviceId) {
        return dataCaptureDeviceInternalService.findOne(deviceId).map(dataCaptureDeviceMapper::toRequest);
    }


//    @Transactional
//    public void updateLatestDeviceData(UUID deviceId, Data data) {
//        dataCaptureDeviceRepository.setLastDataIdAndLastDataTimeByDeviceId(deviceId, data.getId(), data.getServerTime());
//        Cache.deviceIdToLastDataPut(deviceId, data);
//    }
//
//    @Transactional
//    public Optional<Data> findLatestDeviceDataExec(UUID deviceId) {
//        Optional<Data> result = Optional.ofNullable(Cache.deviceIdToLastDataGet(deviceId));
//        if (result.isEmpty()) {
//            result = dataCaptureDeviceRepository.findLatestDeviceData(deviceId);
//            result.ifPresent((data -> Cache.deviceIdToLastDataPut(deviceId, data)));
//        }
//        return result;
//    }

    @Transactional(readOnly = true)
    public Optional<DataCaptureDeviceRequest> findByUniqueId(String uniqueId) {
        return dataCaptureDeviceInternalService.findByUniqueId(uniqueId).map(dataCaptureDeviceMapper::toRequest);
    }


    @Transactional(readOnly = true)
    public Page<DataCaptureDeviceRequest> findByCompany(UUID companyId) {
        Pageable pageable = PageRequest.of(0, 100);
        var result = dataCaptureDeviceInternalService.findByCompanyId(companyId, pageable);
        return new Page<>(result.getContent().stream().map(dataCaptureDeviceMapper::toRequest).toList(),
                result.getTotalElements());
    }
}
