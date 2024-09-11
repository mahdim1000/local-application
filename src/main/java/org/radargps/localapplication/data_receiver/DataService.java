package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.data_receiver.domain.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataService {
    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Transactional
    public Data save(Data data) {
        return dataRepository.save(data);
    }

    public Optional<Data> findById(UUID dataId) {
        return dataRepository.findById(dataId);
    }
}
