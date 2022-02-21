package com.hunter.transactionalannotation.repositories;

import com.hunter.transactionalannotation.entities.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordDataAccess extends JpaRepository<Record, Integer> {
}
