package com.ToDo.backend.repositories;

import com.ToDo.backend.data.entity.Status;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.ToDoItemSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {


    Page<ToDoItem> findAllByUserEmailIs(String email,Pageable pageable);

    Page<ToDoItem> findByTitleContainingIgnoreCaseAndDueDateAfterAndUserEmailIs(String searchQuery,  LocalDate localdDate,String email, Pageable pageable);

    Page<ToDoItem> findByTitleContainingIgnoreCaseAndUserEmailIs(String searchqQery, String email, Pageable pageable);

    Page<ToDoItem> findByUserEmailIsIgnoreCaseAndDueDateAfter(String searchQuery,LocalDate filterDate, Pageable pageable);

    List<ToDoItem> findByHashAndUserEmailIsNot(String hash, String email);

    long countByTitleContainingIgnoreCaseAndUserEmailIsAndDueDateAfter(String searchQuery, String email, LocalDate dueDate);

    long countByTitleContainingIgnoreCaseAndUserEmailIs(String searchQuery, String email);

    long countByDueDateAndUserEmailIs(LocalDate dueDate, String email);

    long countByUserEmailIs(String email);

    long countByDueDateAndStatusInAndUserEmailIs(LocalDate dueDate, Collection<Status> state, String email);

    long countByStatusAndUserEmailIs(Status state, String email);

    List<ToDoItemSummary> findByDueDateGreaterThanEqualAndUserEmailIs(LocalDate dueDate, String email);

    @Query("SELECT day(dueDate) as day, count(*) as deliveries FROM ToDoItem o where o.status=?1 and year(dueDate)=?2 and month(dueDate)=?3 and o.userEmail=?4 group by day(dueDate)")
    List<Object[]> countPerDayAndUserEmailIs(Status status, int year, int month, String email);

    @Query("SELECT count(t.status) from ToDoItem t where status=?1 and user_email=?2 AND year(t.dueDate)=?3 AND month(t.dueDate)=?4")
    long countPerStatusAndEmailIs(Status status, String email,int year, int month);

    @Query("SELECT month(dueDate) as month, count(*) as finished FROM ToDoItem o where o.status=?1 and user_email=?2 and year(dueDate)=?3 group by month(dueDate)")
    List<Object[]> countPerMonth(Status status, String email, int year);

    @Query("SELECT year(o.dueDate) as y, month(o.dueDate) as m, count(*) as finished FROM ToDoItem o  where o.status=?1 and user_email=?2 and year(o.dueDate)<=?3 AND year(o.dueDate)>=(?3-3) group by year(o.dueDate), month(o.dueDate) order by y desc, month(o.dueDate)")
    List<Object[]> countPerMonthLastThreeYears(Status status, String email,int year);
}
