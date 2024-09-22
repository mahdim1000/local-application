package org.radargps.localapplication.data_receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.radargps.localapplication.common.Cache;
import org.radargps.localapplication.common.outbox.OutboxService;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.data_receiver.event.PalletScannned;
import org.radargps.localapplication.data_receiver.event.ProductScanned;
import org.radargps.localapplication.data_receiver.message.publisher.PalletEventPublisher;
import org.radargps.localapplication.data_receiver.message.publisher.ProductEventPublisher;
import org.radargps.localapplication.data_receiver.message.publisher.ProductPalletEventPublisher;
import org.radargps.localapplication.data_receiver.message.publisher.ProductProductEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataCaptureDeviceInternalService {
    private final DataCaptureDeviceRepository dataCaptureDeviceRepository;
//    private final DataService dataService;
//    private final OutboxService outboxService;

    private final PalletEventPublisher palletEventPublisher;
    private final ProductEventPublisher productEventPublisher;
    private final ProductPalletEventPublisher productPalletEventPublisher;
    private final ProductProductEventPublisher productProductEventPublisher;

    public DataCaptureDeviceInternalService(DataCaptureDeviceRepository dataCaptureDeviceRepository,
                                            PalletEventPublisher palletEventPublisher, ProductEventPublisher productEventPublisher, ProductPalletEventPublisher productPalletEventPublisher, ProductProductEventPublisher productProductEventPublisher) {
        this.dataCaptureDeviceRepository = dataCaptureDeviceRepository;
        this.palletEventPublisher = palletEventPublisher;
        this.productEventPublisher = productEventPublisher;
        this.productPalletEventPublisher = productPalletEventPublisher;
        this.productProductEventPublisher = productProductEventPublisher;
    }

    @Transactional
    public DataCaptureDevice create(DataCaptureDevice device) {
        return dataCaptureDeviceRepository.save(device);
    }


    @Transactional
    public DataCaptureDevice updateDevice(DataCaptureDevice device) {
        return dataCaptureDeviceRepository.save(device);
    }

    @Transactional
    public Optional<DataCaptureDevice> findOne(UUID deviceId) {
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
    public Optional<Data> findLatestDeviceData(UUID deviceId) {
        Optional<Data> result = Optional.ofNullable(Cache.deviceIdToLastDataGet(deviceId));
        if (result.isEmpty()) {
            result = dataCaptureDeviceRepository.findLatestDeviceData(deviceId);
            result.ifPresent((data -> Cache.deviceIdToLastDataPut(deviceId, data)));
        }
        return result;
    }

    public Optional<DataCaptureDevice> findByUniqueId(String uniqueId) {
        Optional<DataCaptureDevice> result;
        if (Cache.uniqueIdToDataCaptureDeviceContainsKey(uniqueId)) {
            result = Optional.of(Cache.uniqueIdToDataCaptureDeviceGet(uniqueId));
        } else {
            result = dataCaptureDeviceRepository.findByUniqueId(uniqueId);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Page<DataCaptureDevice> findByCompanyId(UUID companyId, Pageable pageable) {
        var result = dataCaptureDeviceRepository.findByCompanyId(companyId, pageable);
        return new Page<>(result.getContent().stream().toList(),
                result.getTotalElements());
    }

    @Transactional
    public void processAndPublish(Data data) {
        var device = findOne(data.getDeviceId());
        if (device.isPresent()) {
            switch (device.get().getRole()) {
                case PRODUCT_SCANNER -> {
                    productScanned(device.get(), data);
                }
                case PALLET_SCANNER -> {
                    palletScanned(device.get(), data);
                }
                case PRODUCT_PALLET_ASSIGNER -> {
                    productPalletAssigned(device.get(), data);
                }
                case PRODUCT_PRODUCT_ASSIGNER -> {
                    productProductAssigned(device.get(), data);
                }
                case PALLET_UN_ASSIGNER -> {
                    palletUnAssigned(device.get(), data);
                }
                case PRODUCT_UN_ASSIGNER -> {
                    productUnAssigned(device.get(), data);
                }
            }
        }
    }

    private void productScanned(DataCaptureDevice device, Data data) {
        var event = new ProductScanned(device.getUniqueId(), data.getData());
        productEventPublisher.publish(event);
    }
    private void palletScanned(DataCaptureDevice device, Data data) {
        var event = new PalletScannned(device.getUniqueId(), data.getData());
        palletEventPublisher.publish(event);
    }
    private void productPalletAssigned(DataCaptureDevice device, Data data) {
//        var connectedDevice = device.get().getConnectedDevice();
//        event = new ProductPalletAssigned()
    }
    private void productUnAssigned(DataCaptureDevice device, Data data) {

    }
    private void palletUnAssigned(DataCaptureDevice device, Data data) {

    }
    private void productProductAssigned(DataCaptureDevice device, Data data) {

    }
}
