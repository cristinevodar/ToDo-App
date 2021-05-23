package com.ToDo.backend.data;

import com.ToDo.backend.data.entity.Status;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class DashboardDataTasks {
    private FinishedStats finishedStats;
    private List<Number> finishedThisMonth;
    private List<Number> finishedThisYear;
    private Number[][] finishedPerMonth;
    private LinkedHashMap<Status, Integer> TaskStatus;


    public Number[] getFinishedPerMonth(int i) {
        return finishedPerMonth[i];
    }
}
