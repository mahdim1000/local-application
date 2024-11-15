package org.radargps.localapplication.captured.data;

import org.radargps.localapplication.captured.data.domain.Data;
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
    public Data insertData(Data data) {
        return dataRepository.save(data);
    }

    public Optional<Data> findById(UUID dataId) {
        return dataRepository.findById(dataId);
    }
}
