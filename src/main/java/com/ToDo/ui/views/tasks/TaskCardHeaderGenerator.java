package com.ToDo.ui.views.tasks;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.ui.views.tasks.beans.TaskCardHeader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class TaskCardHeaderGenerator {
    private class HeaderWrapper {
        private Predicate<LocalDate> matcher;

        private TaskCardHeader header;

        private Long selected;

        public HeaderWrapper(Predicate<LocalDate> matcher, TaskCardHeader header) {
            this.matcher = matcher;
            this.header = header;
        }

        public boolean matches(LocalDate date) {
            return matcher.test(date);
        }

        public Long getSelected() {
            return selected;
        }

        public void setSelected(Long selected) {
            this.selected = selected;
        }

        public TaskCardHeader getHeader() {
            return header;
        }
    }

    private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

    private final Map<Long, TaskCardHeader> tasksWithHeaders = new HashMap<>();
    private List<HeaderWrapper> headerChain = new ArrayList<>();

    private TaskCardHeader getRecentHeader() {
        return new TaskCardHeader("Recent", "Before this week");
    }

    private TaskCardHeader getYesterdayHeader() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return new TaskCardHeader("Yesterday", secondaryHeaderFor(yesterday));
    }

    private TaskCardHeader getTodayHeader() {
        LocalDate today = LocalDate.now();
        return new TaskCardHeader("Today", secondaryHeaderFor(today));
    }

    private TaskCardHeader getThisWeekBeforeYesterdayHeader() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        return new TaskCardHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
    }

    private TaskCardHeader getThisWeekStartingTomorrow(boolean showPrevious) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
        return new TaskCardHeader(showPrevious ? "This week starting tomorrow" : "This week",
                secondaryHeaderFor(tomorrow, nextWeekStart));
    }

    private TaskCardHeader getUpcomingHeader() {
        return new TaskCardHeader("Upcoming", "After this week");
    }

    private String secondaryHeaderFor(LocalDate date) {
        return HEADER_DATE_TIME_FORMATTER.format(date);
    }

    private String secondaryHeaderFor(LocalDate start, LocalDate end) {
        return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
    }

    public TaskCardHeader get(Long id) {
        return tasksWithHeaders.get(id);
    }

    public void resetHeaderChain(boolean showPrevious) {
        this.headerChain = createHeaderChain(showPrevious);
        tasksWithHeaders.clear();
    }

    public void tasksRead(List<ToDoItem> tasks) {
        Iterator<HeaderWrapper> headerIterator = headerChain.stream().filter(h -> h.getSelected() == null).iterator();
        if (!headerIterator.hasNext()) {
            return;
        }

        HeaderWrapper current = headerIterator.next();
        for (ToDoItem order : tasks) {
            // If last selected, discard orders that match it.
            if (current.getSelected() != null && current.matches(order.getDueDate())) {
                continue;
            }
            while (current != null && !current.matches(order.getDueDate())) {
                current = headerIterator.hasNext() ? headerIterator.next() : null;
            }
            if (current == null) {
                break;
            }
            current.setSelected(order.getId());
            tasksWithHeaders.put(order.getId(), current.getHeader());
        }
    }

    private List<HeaderWrapper> createHeaderChain(boolean showPrevious) {
        List<HeaderWrapper> headerChain = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfTheWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        if (showPrevious) {
            LocalDate yesterday = today.minusDays(1);
            // Week starting on Monday
            headerChain.add(new HeaderWrapper(d -> d.isBefore(startOfTheWeek), this.getRecentHeader()));
            if (startOfTheWeek.isBefore(yesterday)) {
                headerChain.add(new HeaderWrapper(d -> d.isBefore(yesterday) && !d.isAfter(startOfTheWeek),
                        this.getThisWeekBeforeYesterdayHeader()));
            }
            headerChain.add(new HeaderWrapper(yesterday::equals, this.getYesterdayHeader()));
        }
        LocalDate firstDayOfTheNextWeek = startOfTheWeek.plusDays(7);
        headerChain.add(new HeaderWrapper(today::equals, getTodayHeader()));
        headerChain.add(new HeaderWrapper(d -> d.isAfter(today) && d.isBefore(firstDayOfTheNextWeek),
                getThisWeekStartingTomorrow(showPrevious)));
        headerChain.add(new HeaderWrapper(d -> !d.isBefore(firstDayOfTheNextWeek), getUpcomingHeader()));
        return headerChain;
    }
}
