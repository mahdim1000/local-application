package org.radargps.localapplication.scanner.connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.checkerframework.checker.units.qual.A;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ScannerConnectionCriteria {

    @PersistenceContext
    private EntityManager em;

    Page<ScannerConnection> findAll(UUID companyId, ScannerConnectionType type, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ScannerConnection> cr = cb.createQuery(ScannerConnection.class);
        Root<ScannerConnection> scannerConnectionRoot = cr.from(ScannerConnection.class);

        List<Predicate> predicates = new ArrayList<>();
        if (companyId != null) {
            predicates.add(cb.equal(scannerConnectionRoot.get("companyId"), companyId));
        }
        if (type != null) {
            predicates.add(cb.equal(scannerConnectionRoot.get("type"), type));
        }

        cr.select(scannerConnectionRoot).where(predicates.toArray(new Predicate[0]));

        var query = em.createQuery(cr);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        var result = query.getResultList();
        return new PageImpl<>(result, pageable, count(companyId, type));
    }

    private Long count(UUID companyId, ScannerConnectionType type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ScannerConnection> countRoot = countQuery.from(ScannerConnection.class);

        List<Predicate> predicates = new ArrayList<>();
        if (companyId != null) {
            predicates.add(cb.equal(countRoot.get("companyId"), companyId));
        }
        if (type != null) {
            predicates.add(cb.equal(countRoot.get("type"), type));
        }

        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        return em.createQuery(countQuery).getSingleResult();

    }

    public Optional<ScannerConnection> findByFirstScannerIdOrSecondSecannerId(String scannerId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ScannerConnection> cr = cb.createQuery(ScannerConnection.class);
        Root<ScannerConnection> scannerConnectionRoot = cr.from(ScannerConnection.class);

        Predicate predicate = cb.or(
                cb.equal(scannerConnectionRoot.get("firstScanner").get("uniqueId"), scannerId),
                cb.equal(scannerConnectionRoot.get("secondScanner").get("uniqueId"), scannerId)
        );

        cr.select(scannerConnectionRoot).where(predicate);

        var query = em.createQuery(cr);
        return Optional.ofNullable(query.getSingleResult());
    }
}
