package org.radargps.localapplication.scanner.device;

import org.radargps.localapplication.common.Cache;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.temp.ProductPalletScannerConnectionCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.dto.ScannerRequest;
import org.radargps.localapplication.scanner.dto.ScannerUpdateCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerService {
    private final ScannerMapper scannerMapper;
    private final ScannerInternalService scannerInternalService;

    public ScannerService(ScannerMapper scannerMapper,
                          ScannerInternalService scannerInternalService) {
        this.scannerMapper = scannerMapper;
        this.scannerInternalService = scannerInternalService;
    }

    @Transactional
    public ScannerRequest create(ScannerCreateCommand createCommand) {
        var device = scannerMapper.toEntity(createCommand);
        device = scannerInternalService.create(device);
        return scannerMapper.toRequest(device);
    }


    @Transactional
    public ScannerRequest partialUpdate(UUID dataCaptureDeviceId, ScannerUpdateCommand command) {
        var device = scannerInternalService.findOne(dataCaptureDeviceId)
                .orElseThrow(RuntimeException::new);
        scannerMapper.partialUpdate(device, command);
        scannerInternalService.updateDevice(device);
        return scannerMapper.toRequest(device);
    }

    @Transactional
    public Optional<ScannerRequest> findOne(UUID deviceId) {
        return scannerInternalService.findOne(deviceId)
                .map(scannerMapper::toRequest);
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
    public Optional<ScannerRequest> findByUniqueId(String uniqueId) {
        return scannerInternalService.findByUniqueId(uniqueId)
                .map(scannerMapper::toRequest);
    }


    @Transactional(readOnly = true)
    public Page<ScannerRequest> findByCompany(UUID companyId) {
        Pageable pageable = PageRequest.of(0, 100);
        var result = scannerInternalService.findByCompanyId(companyId, pageable);
        return new Page<>(
                result.getContent().stream().map(scannerMapper::toRequest).toList(),
                result.getTotalElements()
        );
    }
}
