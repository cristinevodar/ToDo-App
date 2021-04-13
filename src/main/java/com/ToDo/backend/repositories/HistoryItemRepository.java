package com.ToDo.backend.repositories;

import com.ToDo.backend.data.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
