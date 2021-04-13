package com.ToDo.backend.repositories;

import com.ToDo.backend.data.entity.ToDoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToDoItemRepository extends CrudRepository<ToDoItem, Integer> {
}
