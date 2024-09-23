package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface DataCaptureDeviceRepository extends JpaRepository<DataCaptureDevice, UUID> {

    @Modifying
    @Query("UPDATE DataCaptureDevice d SET d.lastDataId = :dataId, d.lastDataTime = :lastServerTime WHERE d.id = :deviceId")
    void setLastDataIdAndLastDataTimeByDeviceId(UUID deviceId, UUID dataId, Long lastServerTime);

    Optional<DataCaptureDevice> findByUniqueId(String uniqueId);
    Page<DataCaptureDevice> findByCompanyId(UUID companyId, Pageable pageable);

    @Query("SELECT data FROM DataCaptureDevice device INNER JOIN Data data On device.lastDataId = data.id")
    Optional<Data> findLatestDeviceData(UUID deviceId);
}
