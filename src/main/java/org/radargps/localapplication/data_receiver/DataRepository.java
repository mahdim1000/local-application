package org.radargps.localapplication.data_receiver;

import org.radargps.localapplication.data_receiver.domain.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DataRepository extends JpaRepository<Data, UUID> {
}
