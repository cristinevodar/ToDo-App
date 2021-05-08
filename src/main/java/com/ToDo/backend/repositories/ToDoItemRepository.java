package com.ToDo.backend.repositories;

import com.ToDo.backend.data.entity.ToDoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {


    Page<ToDoItem> findAll(Pageable pageable);



    Page<ToDoItem> findByTitleContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate localdDate, Pageable pageable);

    Page<ToDoItem> findByTitleContainingIgnoreCase(String searchqQery,  Pageable pageable);

    Page<ToDoItem> findByDueDateAfter(LocalDate filterDate, Pageable pageable);

    long countByTitleContainingIgnoreCaseAndDueDateAfter(String searchQuery, LocalDate dueDate);

    long countByTitleContainingIgnoreCase(String searchQuery);

    long countByDueDate(LocalDate dueDate);

}
