package org.radargps.localapplication.scanner.device;

import jakarta.persistence.criteria.Predicate;
import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScannerRepository extends JpaRepository<Scanner, String>, JpaSpecificationExecutor<Scanner> {

    @Modifying
    @Query("UPDATE Scanner d SET d.lastDataId = :dataId, d.lastDataValue = :dataValue, d.lastDataTime = :lastServerTime WHERE d.uniqueId = :uniqueId")
    void setLastDataIdAndLastDataTimeByUniqueId(String uniqueId, UUID dataId, String dataValue, Long lastServerTime);

    Optional<Scanner> findByUniqueId(String uniqueId);
    Page<Scanner> findByCompanyId(UUID companyId, Pageable pageable);
    Page<Scanner> findByCompanyIdAndType(UUID companyId, ScannerType type, PageRequest pageRequest);

    @Query("SELECT data FROM Scanner scanner INNER JOIN Data data On scanner.lastDataId = data.id WHERE scanner.uniqueId = :uniqueId")
    Optional<Data> findLatestScannerData(String uniqueId);


    class Specifications {
        public static Specification<Scanner> withCompanyIdAndType(UUID companyId, ScannerType type) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Always add companyId condition
                predicates.add(criteriaBuilder.equal(root.get("companyId"), companyId));

                // Add type condition only if type is not null
                if (type != null) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}
