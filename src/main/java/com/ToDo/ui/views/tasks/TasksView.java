package com.ToDo.ui.views.tasks;

import com.ToDo.app.HasLogger;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.util.EntityUtil;
import com.ToDo.ui.MainView;
import com.ToDo.ui.components.SearchBar;
import com.ToDo.ui.utils.TasksConst;
import com.ToDo.ui.views.EntityView;
import com.ToDo.ui.views.taskedit.TaskDetails;
import com.ToDo.ui.views.taskedit.TaskEditor;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@Tag("tasks-view")
@JsModule("./src/views/tasks/tasks-view.js")
@Route(value = TasksConst.PAGE_TASKS, layout = MainView.class)
@RouteAlias(value = TasksConst.PAGE_TASKS_EDIT, layout = MainView.class)
@RouteAlias(value = TasksConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(TasksConst.TITLE_TASKS)
public class TasksView extends PolymerTemplate<TemplateModel>
        implements HasLogger, HasUrlParameter<Long>, EntityView<ToDoItem> {

    @Id("search")
    private SearchBar searchBar;

    @Id("grid")
    private Grid<ToDoItem> grid;

    @Id("dialog")
    private Dialog dialog;

    private ConfirmDialog confirmation;

    private final TaskEditor taskEditor;

    private final TaskDetails taskDetails = new TaskDetails();

    private final TaskPresenter presenter ;

    @Autowired
    public TasksView(TaskPresenter presenter,TaskEditor taskEditor ) {
        this.presenter = presenter;
        this.taskEditor = taskEditor;


        searchBar.setActionText("New Task");
        searchBar.setCheckboxText("Show past tasks");
        searchBar.setPlaceHolder("Search");

        grid.setSelectionMode(Grid.SelectionMode.NONE);


        grid.addColumn(TaskCard.getTemplate()
                .withProperty("taskCard", TaskCard::create)
                .withProperty("header", task -> presenter.getHeaderByTaskId(task.getId()))
                .withEventHandler("cardClick",
                        task -> UI.getCurrent().navigate(TasksConst.PAGE_TASKS + "/" + task.getId())));

        getSearchBar().addFilterChangeListener(
                e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
        getSearchBar().addActionClickListener(e -> presenter.createNewTask());

        presenter.init(this);

        dialog.addDialogCloseActionListener(e -> presenter.cancel());
    }

    @Override
    public boolean isDirty() {
        return taskEditor.hasChanges() || taskDetails.isDirty();
    }

    @Override
    public void clear() {
        taskDetails.setDirty(false);
        taskEditor.clear();
    }

    void navigateToMainView() {
        getUI().ifPresent(ui -> ui.navigate(TasksConst.PAGE_TASKS));
    }


    @Override
    public void write(ToDoItem entity) throws ValidationException {
        taskEditor.write(entity);
    }

    @Override
    public String getEntityName() {
        return EntityUtil.getName(ToDoItem.class);
    }

    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    @Override
    public ConfirmDialog getConfirmDialog() {
        return confirmation;
    }



    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long toDoItemId) {
        boolean editView = event.getLocation().getPath().contains(TasksConst.PAGE_TASKS_EDIT);
        if (toDoItemId != null) {
            presenter.onNavigation(toDoItemId, editView);
        } else if (dialog.isOpened()) {
            presenter.closeSilently();
        }
    }

    TaskEditor getOpenedTaskEditor() {
        return taskEditor;
    }

    TaskDetails getOpenedTaskDetails() {
        return taskDetails;
    }

    void setDialogElementsVisibility(boolean editing) {
        dialog.add(editing ? taskEditor : taskDetails);
        taskEditor.setVisible(editing);
        taskDetails.setVisible(!editing);
    }

    Grid<ToDoItem> getGrid() {
        return grid;
    }

    void setOpened(boolean opened) {
        dialog.setOpened(opened);
    }

    SearchBar getSearchBar() {
        return searchBar;
    }

    public Stream<HasValue<?, ?>> validate() {
        return taskEditor.validate();
    }

}
