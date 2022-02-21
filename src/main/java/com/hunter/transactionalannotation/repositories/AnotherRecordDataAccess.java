package com.hunter.transactionalannotation.repositories;

import com.hunter.transactionalannotation.entities.AnotherRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnotherRecordDataAccess extends JpaRepository<AnotherRecord, Integer> {
}
