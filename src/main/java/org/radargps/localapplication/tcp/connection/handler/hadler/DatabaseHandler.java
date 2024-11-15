package org.radargps.localapplication.tcp.connection.handler.hadler;

import org.radargps.localapplication.captured.data.DataService;
import org.radargps.localapplication.captured.data.domain.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DatabaseHandler extends BaseDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
    private final DataService dataService;

    public DatabaseHandler(DataService dataService) {
        this.dataService = dataService;
    }


    @Override
    public void handleReceivedData(Data data, Callback callback) {
        data = dataService.insertData(data);
        callback.processed(false);
    }

}
