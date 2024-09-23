package org.radargps.localapplication.scanner.device;

import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ScannerRepository extends JpaRepository<Scanner, UUID> {

    @Modifying
    @Query("UPDATE Scanner d SET d.lastDataId = :dataId, d.lastDataTime = :lastServerTime WHERE d.id = :deviceId")
    void setLastDataIdAndLastDataTimeByDeviceId(UUID deviceId, UUID dataId, Long lastServerTime);

    Optional<Scanner> findByUniqueId(String uniqueId);
    Page<Scanner> findByCompanyId(UUID companyId, Pageable pageable);

    @Query("SELECT data FROM Scanner device INNER JOIN Data data On device.lastDataId = data.id")
    Optional<Data> findLatestDeviceData(UUID deviceId);
}
