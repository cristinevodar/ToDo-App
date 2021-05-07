package com.ToDo.ui.views.tasks.beans;

public class TasksCountDataWithChart extends TasksCountData {
    private Integer overall;

    public TasksCountDataWithChart() {

    }

    public TasksCountDataWithChart(String title, String subtitle, Integer count, Integer overall) {
        super(title, subtitle, count);
        this.overall = overall;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }
}
