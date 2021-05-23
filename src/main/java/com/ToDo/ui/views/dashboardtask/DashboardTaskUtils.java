package com.ToDo.ui.views.dashboardtask;

import com.ToDo.backend.data.FinishedStats;
import com.ToDo.backend.data.entity.Status;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.ToDoItemSummary;
import com.ToDo.ui.views.tasks.beans.TasksCountData;
import com.ToDo.ui.views.tasks.beans.TasksCountDataWithChart;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

public class DashboardTaskUtils {

    private static final String NEXT_TASK_PATTERN="Next Task %s";

    public static TasksCountDataWithChart getTodaysTasksCountData(FinishedStats finishedStats,
                                                                  Iterator<ToDoItemSummary> tasksIterator) {
        TasksCountDataWithChart tasksCountData = new TasksCountDataWithChart("Remaining Today", null,
                finishedStats.getDueToday() - finishedStats.getFinishedToday(), finishedStats.getDueToday());

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        while (tasksIterator.hasNext()) {

            ToDoItemSummary toDoItemSummary = tasksIterator.next();
            if (isTaskNextToBeFinished(toDoItemSummary, date, time)) {
                if (toDoItemSummary.getDueDate().isEqual(date))
                    tasksCountData.setSubtitle(String.format(NEXT_TASK_PATTERN, toDoItemSummary.getDueTime()));
                else
                    tasksCountData.setSubtitle(String.format(NEXT_TASK_PATTERN,
                            toDoItemSummary.getDueDate().getMonthValue() + "/" + toDoItemSummary.getDueDate().getDayOfMonth()));

                break;
            }

        }
        return tasksCountData;
    }

    private static boolean isTaskNextToBeFinished(ToDoItemSummary task,LocalDate nowDate, LocalTime nowTime) {
        return task.getStatus() == Status.NEW
                && ((task.getDueDate().isEqual(nowDate) && task.getDueTime().isAfter(nowTime))
                || task.getDueDate().isAfter(nowDate));
    }

    public static TasksCountData getNotFinishedTasksCountData(FinishedStats finishedStats) {
        TasksCountData tasksCountData = new TasksCountData("In Progress", "Deadline tomorrow",
                finishedStats.getInProgressToday());

        return tasksCountData;
    }

    public static TasksCountData getNewTasksCountData(FinishedStats finishedStats, ToDoItem lastTask) {
        return new TasksCountData("New", "subtitle", finishedStats.getNewTasks());
    }

    public static TasksCountData getTomorrowTasksCountData(FinishedStats finishedStats,
                                                             Iterator<ToDoItemSummary> tasksIterator) {
        TasksCountData tasksCountData = new TasksCountData("Tomorrow", null, finishedStats.getDueTomorrow());

        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime minTime = LocalTime.MAX;
        while (tasksIterator.hasNext()) {
            ToDoItemSummary task = tasksIterator.next();
            if (task.getDueDate().isBefore(date)) {
                continue;
            }

            if (task.getDueDate().isEqual(date)) {
                if (task.getDueTime().isBefore(minTime)) {
                    minTime = task.getDueTime();
                }
            }

            if (task.getDueDate().isAfter(date)) {
                break;
            }
        }

        if (!LocalTime.MAX.equals(minTime))
            tasksCountData.setSubtitle("First deadline " + minTime);

        return tasksCountData;
    }
}
