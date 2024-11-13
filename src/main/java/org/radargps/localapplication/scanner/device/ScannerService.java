package org.radargps.localapplication.scanner.device;

import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.common.constants.PageConstants;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.radargps.localapplication.scanner.device.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerRequest;
import org.radargps.localapplication.scanner.device.dto.ScannerUpdateCommand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.radargps.localapplication.common.constants.PageConstants.MAX_PAGE_SIZE;

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
    public ScannerRequest partialUpdate(String uniqueId, ScannerUpdateCommand command) {
        var scanner = scannerInternalService.findOne(uniqueId)
                .orElseThrow(ResourceNotFoundException::new);
        scannerMapper.partialUpdate(scanner, command);
        scannerInternalService.updateDevice(scanner);
        return scannerMapper.toRequest(scanner);
    }

    @Transactional
    public Optional<ScannerRequest> findOne(String uniqueId) {
        return scannerInternalService.findOne(uniqueId)
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

    @Transactional(readOnly = true)
    public Page<ScannerRequest> findAll(UUID companyId, ScannerType type,
                                        String sortBy, String sortDirection, Integer pageNumber, Integer pageSize) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy != null && !sortBy.isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.fromString(sortDirection), sortBy));
        }
        PageRequest pageRequest = PageRequest.of(
                pageNumber,
                Math.min(pageSize, MAX_PAGE_SIZE),
                Sort.by(orders)
        );

        var result = scannerInternalService.findAll(companyId, type, pageRequest);
        return new Page<>(
                result.getContent().stream().map(scannerMapper::toRequest).toList(),
                result.getTotalElements()
        );
    }

    /**
     * Delete a single scanner
     */
    @Transactional
    public void deleteScanner(String uniqueId) {
        scannerInternalService.deleteScanner(uniqueId);
    }
}
