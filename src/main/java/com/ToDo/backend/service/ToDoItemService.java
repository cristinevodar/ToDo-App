package com.ToDo.backend.service;

import com.ToDo.backend.data.DashboardDataTasks;
import com.ToDo.backend.data.FinishedStats;
import com.ToDo.backend.data.entity.*;
import com.ToDo.backend.repositories.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.BiConsumer;

@Service
public class ToDoItemService implements CrudService<ToDoItem> {

    @Autowired
    UserSession currentUser;

    private final ToDoItemRepository toDoItemRepository;

    @Autowired
    public ToDoItemService(ToDoItemRepository toDoItemRepository) {
        super();
        this.toDoItemRepository = toDoItemRepository;
    }

    @Override
    public JpaRepository<ToDoItem, Long> getRepository() {
        return toDoItemRepository;
    }

    @Override
    public ToDoItem save(User currentUser, ToDoItem toDoItem) {

        toDoItem.setCreatedBy(currentUser.getEmail());
        if ( toDoItem.getHash()==null) {
            if (!toDoItem.getUsers().isEmpty()) {
                toDoItem.setHash(UUID.randomUUID().toString());
                ToDoItem task2 = new ToDoItem();
                task2.setUserEmail(toDoItem.getUsers());
                task2.setTitle(toDoItem.getTitle());
                task2.setDescription(toDoItem.getDescription());
                task2.setPriority(toDoItem.getPriority());
                task2.setDueTime(toDoItem.getDueTime());
                task2.setDueDate(toDoItem.getDueDate());
                task2.setHash(toDoItem.getHash());
                toDoItemRepository.save(task2);
            }
        }
        else {
            for ( ToDoItem task2: toDoItemRepository.findByHashAndUserEmailIsNot(toDoItem.getHash(),toDoItem.getUserEmail())){
                task2.setTitle(toDoItem.getTitle());
                task2.setDescription(toDoItem.getDescription());
                task2.setPriority(toDoItem.getPriority());
                task2.setStatus(toDoItem.getStatus());
                task2.setDueTime(toDoItem.getDueTime());
                task2.setDueDate(toDoItem.getDueDate());
                task2.setHash(toDoItem.getHash());
                toDoItemRepository.save(task2);
            }
        }

        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public void delete(User currentUser, ToDoItem entity) {

    }

    @Transactional(rollbackOn = Exception.class)
    public ToDoItem saveToDoItem(User currentUser, Long id, BiConsumer<User, ToDoItem> orderFiller) {
        ToDoItem toDoItem;
        if (id == null) {
            toDoItem = new ToDoItem();
        } else {
            toDoItem = load(id);
        }
        toDoItem.setUserEmail(currentUser.getEmail());
        orderFiller.accept(currentUser, toDoItem);
        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public void delete(User currentUser, long id) {

    }

    public Page<ToDoItem> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
                                                      Optional<LocalDate> optionalFilterDate, Pageable pageable) {

        if (optionalFilter.isPresent() && !optionalFilter.get().isEmpty()) {
            if (optionalFilterDate.isPresent()) {
                return toDoItemRepository.findByTitleContainingIgnoreCaseAndDueDateAfterAndUserEmailIs(
                        optionalFilter.get(), optionalFilterDate.get(), currentUser.getUser().getEmail(), pageable);
            } else {
                return toDoItemRepository.findByTitleContainingIgnoreCaseAndUserEmailIs(optionalFilter.get(), currentUser.getUser().getEmail(), pageable);
            }
        } else {
            if (optionalFilterDate.isPresent()) {
                return toDoItemRepository.findByUserEmailIsIgnoreCaseAndDueDateAfter(currentUser.getUser().getEmail(), optionalFilterDate.get(), pageable);
            } else {
                return toDoItemRepository.findAllByUserEmailIs(currentUser.getUser().getEmail(), pageable);
            }
        }
    }

    public long countAnyMatchingAfterDueDate(Optional<String> optionalFilter, Optional<LocalDate> optionalFilterDate) {
        if (optionalFilter.isPresent() && optionalFilterDate.isPresent()) {
            return toDoItemRepository.countByTitleContainingIgnoreCaseAndUserEmailIsAndDueDateAfter(optionalFilter.get(), currentUser.getUser().getEmail(),
                    optionalFilterDate.get());
        } else if (optionalFilter.isPresent()) {
            return toDoItemRepository.countByTitleContainingIgnoreCaseAndUserEmailIs(optionalFilter.get(), currentUser.getUser().getEmail());
        } else if (optionalFilterDate.isPresent()) {
            return toDoItemRepository.countByDueDateAndUserEmailIs(optionalFilterDate.get(), currentUser.getUser().getEmail());
        } else {
            return toDoItemRepository.countByUserEmailIs(currentUser.getUser().getEmail());
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

    @Transactional
    public List<ToDoItemSummary> findAnyMatchingStartingToday() {
        return toDoItemRepository.findByDueDateGreaterThanEqualAndUserEmailIs(LocalDate.now(), currentUser.getUser().getEmail());
    }

    @Override
    public ToDoItem createNew(User currentUser) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setDueTime(LocalTime.of(16, 0));
        toDoItem.setDueDate(LocalDate.now());
        toDoItem.setUserEmail(currentUser.getEmail());
        return toDoItem;
    }

    public DashboardDataTasks getDashboardData(int month, int year) {
        DashboardDataTasks data = new DashboardDataTasks();
        data.setFinishedStats(getFinishedStats());
        data.setFinishedThisMonth(getFinishedPerDay(month, year));
        data.setFinishedThisYear(getFinishedPerMonth(year));

        Number[][] finishedPerMonth = new Number[3][12];
        data.setFinishedPerMonth(finishedPerMonth);
        List<Object[]> finished = toDoItemRepository.countPerMonthLastThreeYears(Status.FINISHED, currentUser.getUser().getEmail(), year);

        for (Object[] finishedData : finished) {
            // year, month, deliveries
            int y = year - (int) finishedData[0];
            int m = (int) finishedData[1] - 1;
//            if (y == 0 && m == month - 1) {
//                // skip current month as it contains incomplete data
//                continue;
//            }
            long count = (long) finishedData[2];
            finishedPerMonth[y][m] = count;
        }

        LinkedHashMap<Status, Integer> taskStatuses = new LinkedHashMap<>();
        data.setTaskStatus(taskStatuses);
        long result1 = toDoItemRepository.countPerStatusAndEmailIs(Status.NEW, currentUser.getUser().getEmail(), year, month);
        taskStatuses.put(Status.NEW, (int) result1);
        long result2 = toDoItemRepository.countPerStatusAndEmailIs(Status.INPROGRESS, currentUser.getUser().getEmail(), year, month);
        taskStatuses.put(Status.INPROGRESS, (int) result2);
        long result3 = toDoItemRepository.countPerStatusAndEmailIs(Status.FINISHED, currentUser.getUser().getEmail(), year, month);
        taskStatuses.put(Status.FINISHED, (int) result3);


        return data;
    }

    private List<Number> getFinishedPerMonth(int year) {
        return flattenAndReplaceMissingWithNull(12, toDoItemRepository.countPerMonth(Status.FINISHED, currentUser.getUser().getEmail(), year));
    }

    private FinishedStats getFinishedStats() {
        FinishedStats stats = new FinishedStats();
        LocalDate today = LocalDate.now();
        stats.setDueToday((int) toDoItemRepository.countByDueDateAndUserEmailIs(today, currentUser.getUser().getEmail()));
        stats.setDueTomorrow((int) toDoItemRepository.countByDueDateAndUserEmailIs(today.plusDays(1), currentUser.getUser().getEmail()));
        stats.setFinishedToday((int) toDoItemRepository.countByDueDateAndStatusInAndUserEmailIs(today,
                Collections.singleton(Status.FINISHED), currentUser.getUser().getEmail()));

        stats.setInProgressToday((int) toDoItemRepository.countByDueDateAndStatusInAndUserEmailIs(today, Collections.singleton(Status.INPROGRESS), currentUser.getUser().getEmail()));
        stats.setNewTasks((int) toDoItemRepository.countByStatusAndUserEmailIs(Status.NEW, currentUser.getUser().getEmail()));

        return stats;
    }


    private List<Number> flattenAndReplaceMissingWithNull(int length, List<Object[]> list) {
        List<Number> counts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            counts.add(null);
        }

        for (Object[] result : list) {
            counts.set((Integer) result[0] - 1, (Number) result[1]);
        }
        return counts;
    }

    private List<Number> getFinishedPerDay(int month, int year) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        return flattenAndReplaceMissingWithNull(daysInMonth,
                toDoItemRepository.countPerDayAndUserEmailIs(Status.FINISHED, year, month, currentUser.getUser().getEmail()));
    }


}
