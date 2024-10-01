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

public interface ScannerRepository extends JpaRepository<Scanner, String> {

    @Modifying
    @Query("UPDATE Scanner d SET d.lastDataId = :dataId, d.lastDataValue = :dataValue, d.lastDataTime = :lastServerTime WHERE d.uniqueId = :uniqueId")
    void setLastDataIdAndLastDataTimeByUniqueId(String uniqueId, UUID dataId, String dataValue, Long lastServerTime);

    Optional<Scanner> findByUniqueId(String uniqueId);
    Page<Scanner> findByCompanyId(UUID companyId, Pageable pageable);

    @Query("SELECT data FROM Scanner scanner INNER JOIN Data data On scanner.lastDataId = data.id WHERE scanner.uniqueId = :uniqueId")
    Optional<Data> findLatestScannerData(String uniqueId);
}
