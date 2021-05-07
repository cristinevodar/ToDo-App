package com.ToDo.backend.service;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.User;
import com.ToDo.backend.repositories.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class ToDoItemService implements CrudService<ToDoItem> {

    private final ToDoItemRepository toDoItemRepository;

    @Autowired
    public ToDoItemService(ToDoItemRepository toDoItemRepository){
        super();
        this.toDoItemRepository=toDoItemRepository;
    }

    @Override
    public JpaRepository<ToDoItem, Long> getRepository() {
        return toDoItemRepository;
    }

    @Override
    public ToDoItem save(User currentUser, ToDoItem toDoItem) {
        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public void delete(User currentUser, ToDoItem entity) {

    }

    @Transactional(rollbackOn = Exception.class)
    public ToDoItem saveOrder(User currentUser, Long id, BiConsumer<User, ToDoItem> orderFiller) {
        ToDoItem order;
        if (id == null) {
            order = new ToDoItem();
        } else {
            order = load(id);
        }
        orderFiller.accept(currentUser, order);
        return toDoItemRepository.save(order);
    }

    @Override
    public void delete(User currentUser, long id) {

    }

    public Page<ToDoItem> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
                                                   Optional<LocalDate> optionalFilterDate, Pageable pageable) {
        if (optionalFilter.isPresent() && !optionalFilter.get().isEmpty()) {
            if (optionalFilterDate.isPresent()) {
                return toDoItemRepository.findByTitleContainingIgnoreCaseAndDueDateAfter(
                        optionalFilter.get(), optionalFilterDate.get(), pageable);
            } else {
                return toDoItemRepository.findByTitleContainingIgnoreCase(optionalFilter.get(), pageable);
            }
        } else {
            if (optionalFilterDate.isPresent()) {
                return toDoItemRepository.findByDueDateAfter(optionalFilterDate.get(), pageable);
            } else {
                return toDoItemRepository.findAll(pageable);
            }
        }
    }

    public long countAnyMatchingAfterDueDate(Optional<String> optionalFilter, Optional<LocalDate> optionalFilterDate) {
        if (optionalFilter.isPresent() && optionalFilterDate.isPresent()) {
            return toDoItemRepository.countByTitleContainingIgnoreCaseAndDueDateAfter(optionalFilter.get(),
                    optionalFilterDate.get());
        } else if (optionalFilter.isPresent()) {
            return toDoItemRepository.countByTitleContainingIgnoreCase(optionalFilter.get());
        } else if (optionalFilterDate.isPresent()) {
            return toDoItemRepository.countByDueDate(optionalFilterDate.get());
        } else {
            return toDoItemRepository.count();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public ToDoItem addComment(User currentUser, ToDoItem task, String comment) {
        task.addHistoryItem(currentUser, comment);
        return toDoItemRepository.save(task);
    }

    @Override
    public long count() {
        return 0;
    }


    @Override
    public ToDoItem createNew(User currentUser) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setDueTime(LocalTime.of(16, 0));
        toDoItem.setDueDate(LocalDate.now());
        return toDoItem;
    }
}
