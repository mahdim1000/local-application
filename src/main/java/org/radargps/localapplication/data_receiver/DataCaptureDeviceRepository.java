package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
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
}
