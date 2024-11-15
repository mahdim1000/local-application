package org.radargps.localapplication.tcp.connection.handler.hadler;

import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PublishDataHandler extends BaseDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private final ScannerInternalService scannerInternalService;

    public PublishDataHandler(ScannerInternalService scannerInternalService) {
        this.scannerInternalService = scannerInternalService;
    }


    @Override
    public void handleReceivedData(Data data, Callback callback) {
        scannerInternalService.processAndPublish(data);
        callback.processed(false);
    }

}