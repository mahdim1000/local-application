package org.radargps.localapplication.captured.data;

import org.radargps.localapplication.captured.data.domain.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DataRepository extends JpaRepository<Data, UUID> {
}
