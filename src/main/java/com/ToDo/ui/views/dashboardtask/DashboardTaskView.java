package com.ToDo.ui.views.dashboardtask;

import com.ToDo.backend.data.DashboardData;
import com.ToDo.backend.data.DashboardDataTasks;
import com.ToDo.backend.data.FinishedStats;
import com.ToDo.backend.data.entity.Status;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.ToDoItemSummary;
import com.ToDo.backend.service.ToDoItemService;
import com.ToDo.ui.MainView;
import com.ToDo.ui.dataproviders.TasksGridDataProvider;
import com.ToDo.ui.utils.BakeryConst;
import com.ToDo.ui.utils.FormattingUtils;
import com.ToDo.ui.views.dashboard.DashboardCounterLabel;
import com.ToDo.ui.views.dashboard.DataSeriesItemWithRadius;
import com.ToDo.ui.views.tasks.TaskCard;
import com.ToDo.ui.views.tasks.beans.TasksCountDataWithChart;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Tag("dashboard-view")
@JsModule("./src/views/dashboard/dashboard-view.js")
@Route(value = BakeryConst.PAGE_TASK_DASHBOARD, layout = MainView.class)
@PageTitle("TaskDashboard")
public class DashboardTaskView extends PolymerTemplate<TemplateModel> {

    private static final String[] MONTH_LABELS = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sep", "Oct", "Nov", "Dec"};

    private final ToDoItemService toDoItemService;

    @Id("todayCount")
    private DashboardTaskCounterLabel todayCount;

    @Id("notAvailableCount")
    private DashboardTaskCounterLabel notAvailableCount;

    @Id("newCount")
    private DashboardTaskCounterLabel newCount;

    @Id("tomorrowCount")
    private DashboardTaskCounterLabel tomorrowCount;

    @Id("deliveriesThisMonth")
    private Chart deliveriesThisMonthChart;

    @Id("deliveriesThisYear")
    private Chart deliveriesThisYearChart;

    @Id("yearlySalesGraph")
    private Chart yearlySalesGraph;

    @Id("ordersGrid")
    private Grid<ToDoItem> grid;

    @Id("monthlyProductSplit")
    private Chart monthlyProductSplit;

    @Id("todayCountChart")
    private Chart todayCountChart;

    @Autowired
    public DashboardTaskView(ToDoItemService toDoItemService, TasksGridDataProvider tasksDataProvider) {
        this.toDoItemService = toDoItemService;

        grid.addColumn(TaskCard.getTemplate()
                .withProperty("taskCard", TaskCard::create)
                .withProperty("header", order -> null)
                .withEventHandler("cardClick",
                        task-> UI.getCurrent().navigate(BakeryConst.PAGE_TASKS + "/" + task.getId())));

        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setDataProvider(tasksDataProvider);

        DashboardDataTasks data = toDoItemService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
       populateYearlyFinishedChart(data);
         populateDeliveriesCharts(data);
         populateTasksCounts(data.getFinishedStats());
         initProductSplitMonthlyGraph(data.getTaskStatus());




    }



    private void populateTasksCounts(FinishedStats finishedStats) {
        List<ToDoItemSummary> tasks = toDoItemService.findAnyMatchingStartingToday();

     TasksCountDataWithChart todaysTasksCountData = DashboardTaskUtils
                .getTodaysTasksCountData(finishedStats, tasks.iterator());
        todayCount.setTasksCountData(todaysTasksCountData);
        initTodayCountSolidgaugeChart(todaysTasksCountData);
        notAvailableCount.setTasksCountData(DashboardTaskUtils.getNotFinishedTasksCountData(finishedStats));
        ToDoItem lastTask = toDoItemService.load(tasks.get(tasks.size() - 1).getId());
        newCount.setTasksCountData(DashboardTaskUtils.getNewTasksCountData(finishedStats, lastTask));
       tomorrowCount.setTasksCountData(DashboardTaskUtils.getTomorrowTasksCountData(finishedStats, tasks.iterator()));
    }

    private void initTodayCountSolidgaugeChart(TasksCountDataWithChart data) {
        Configuration configuration = todayCountChart.getConfiguration();
        configuration.getChart().setType(ChartType.SOLIDGAUGE);
        configuration.setTitle("");
        configuration.getTooltip().setEnabled(false);

        configuration.getyAxis().setMin(0);
        configuration.getyAxis().setMax(data.getOverall());
        configuration.getyAxis().getLabels().setEnabled(false);

        PlotOptionsSolidgauge opt = new PlotOptionsSolidgauge();
        opt.getDataLabels().setEnabled(false);
        configuration.setPlotOptions(opt);

        DataSeriesItemWithRadius point = new DataSeriesItemWithRadius();
        point.setY(data.getCount());
        point.setInnerRadius("100%");
        point.setRadius("110%");
        configuration.setSeries(new DataSeries(point));

        Pane pane = configuration.getPane();
        pane.setStartAngle(0);
        pane.setEndAngle(360);

        Background background = new Background();
        background.setShape(BackgroundShape.ARC);
        background.setInnerRadius("100%");
        background.setOuterRadius("110%");
        pane.setBackground(background);
    }

    private void initProductSplitMonthlyGraph(Map<Status, Integer> taskStatus) {

        LocalDate today = LocalDate.now();

        Configuration conf = monthlyProductSplit.getConfiguration();
        conf.getChart().setType(ChartType.PIE);
        conf.getChart().setBorderRadius(4);
        conf.setTitle("Task statuses in " + FormattingUtils.getFullMonthName(today));
        DataSeries deliveriesPerProductSeries = new DataSeries(taskStatus.entrySet().stream()
                .map(e -> new DataSeriesItem(e.getKey().getDisplayName(), e.getValue())).collect(Collectors.toList()));
        PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
        plotOptionsPie.setInnerSize("60%");
        plotOptionsPie.getDataLabels().setCrop(false);
        deliveriesPerProductSeries.setPlotOptions(plotOptionsPie);
        conf.addSeries(deliveriesPerProductSeries);
    }


    private void populateDeliveriesCharts(DashboardDataTasks data) {
        LocalDate today = LocalDate.now();

        // init the 'Deliveries in [this year]' chart
        Configuration yearConf = deliveriesThisYearChart.getConfiguration();
        configureColumnChart(yearConf);

        yearConf.setTitle("Tasks finished " + today.getYear());
        yearConf.getxAxis().setCategories(MONTH_LABELS);
        yearConf.addSeries(new ListSeries("per Month", data.getFinishedThisYear()));

        // init the 'Deliveries in [this month]' chart
        Configuration monthConf = deliveriesThisMonthChart.getConfiguration();
        configureColumnChart(monthConf);

        List<Number> deliveriesThisMonth = data.getFinishedThisMonth();
        String[] deliveriesThisMonthCategories = IntStream.rangeClosed(1, deliveriesThisMonth.size())
                .mapToObj(String::valueOf).toArray(String[]::new);

        monthConf.setTitle("Tasks finished in " + FormattingUtils.getFullMonthName(today));
        monthConf.getxAxis().setCategories(deliveriesThisMonthCategories);
        monthConf.addSeries(new ListSeries("per Day", deliveriesThisMonth));
    }

    private void configureColumnChart(Configuration conf) {
        conf.getChart().setType(ChartType.COLUMN);
        conf.getChart().setBorderRadius(4);

        conf.getxAxis().setTickInterval(1);
        conf.getxAxis().setMinorTickLength(0);
        conf.getxAxis().setTickLength(0);

        conf.getyAxis().getTitle().setText(null);

        conf.getLegend().setEnabled(false);
    }

    private void populateYearlyFinishedChart(DashboardDataTasks data) {
        Configuration conf = yearlySalesGraph.getConfiguration();
        conf.getChart().setType(ChartType.AREASPLINE);
        conf.getChart().setBorderRadius(4);

        conf.setTitle("Sales last years");

        conf.getxAxis().setVisible(false);
        conf.getxAxis().setCategories(MONTH_LABELS);

        conf.getyAxis().getTitle().setText(null);

        int year = Year.now().getValue();
        for (int i = 0; i < 3; i++) {
            conf.addSeries(new ListSeries(Integer.toString(year - i),data.getFinishedPerMonth(i)));
        }
    }
}
