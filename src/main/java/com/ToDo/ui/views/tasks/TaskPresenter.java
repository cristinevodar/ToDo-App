package com.ToDo.ui.views.tasks;


import com.ToDo.app.security.CurrentUser;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.service.ToDoItemService;
import com.ToDo.ui.crud.EntityPresenter;
import com.ToDo.ui.dataproviders.TasksGridDataProvider;
import com.ToDo.ui.utils.BakeryConst;
import com.ToDo.ui.views.tasks.beans.TaskCardHeader;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskPresenter {

    private TaskCardHeaderGenerator headersGenerator;
    private TasksView view;


    private final EntityPresenter<ToDoItem, TasksView> entityPresenter;
    private final TasksGridDataProvider dataProvider;
    private final CurrentUser currentUser;
    private final ToDoItemService toDoItemService;

    @Autowired
    TaskPresenter(ToDoItemService toDoItemService, TasksGridDataProvider dataProvider, EntityPresenter<ToDoItem, TasksView> entityPresenter,  CurrentUser currentUser ) {
        this.toDoItemService = toDoItemService;
        this.entityPresenter = entityPresenter;
        this.dataProvider = dataProvider;
        this.currentUser = currentUser;
        headersGenerator= new TaskCardHeaderGenerator();
        headersGenerator.resetHeaderChain(false);
        dataProvider.setPageObserver(p->headersGenerator.tasksRead(p.getContent()));
    }

    void init(TasksView view){
        this.entityPresenter.setView(view);
        this.view=view;

        view.getGrid().setDataProvider(dataProvider);
        view.getOpenedTaskEditor().setCurrentUser(currentUser.getUser());
        view.getOpenedTaskEditor().addCancelListener(e -> cancel());
        view.getOpenedTaskEditor().addReviewListener(e -> review());
        view.getOpenedTaskDetails().addSaveListenter(e -> save());
        view.getOpenedTaskDetails().addCancelListener(e -> cancel());
        view.getOpenedTaskDetails().addBackListener(e -> back());
        view.getOpenedTaskDetails().addEditListener(e -> edit());
    }

    TaskCardHeader getHeaderByTaskId(Long id) {
        return headersGenerator.get(id);
    }

    public void filterChanged(String filter, boolean showPrevious) {
        headersGenerator.resetHeaderChain(showPrevious);
        dataProvider.setFilter(new TasksGridDataProvider.TaskFilter(filter, showPrevious));
    }

    void closeSilently() {
        entityPresenter.close();
        view.setOpened(false);
    }

    void onNavigation(Long id, boolean edit) {
        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    void createNewTask() {
        open(entityPresenter.createNew(), true);
    }

    void edit() {
        UI.getCurrent().navigate(BakeryConst.PAGE_TASKS_EDIT + "/" + entityPresenter.getEntity().getId());
    }


    void cancel() {
        entityPresenter.cancel(() -> close(), () -> view.setOpened(true));
    }

    private void close() {

        view.setOpened(false);
        view.navigateToMainView();
        entityPresenter.close();
    }

    void review() {
        // Using collect instead of findFirst to assure all streams are
        // traversed, and every validation updates its view
        List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
        if (fields.isEmpty()) {
            if (entityPresenter.writeEntity()) {
                view.setDialogElementsVisibility(false);
                view.getOpenedTaskDetails().display(entityPresenter.getEntity(), true);
            }
        } else if (fields.get(0) instanceof Focusable) {
            ((Focusable<?>) fields.get(0)).focus();
        }
    }

    void save() {
        entityPresenter.save(e -> {
            if (entityPresenter.isNew()) {
                view.showCreatedNotification();
                dataProvider.refreshAll();
            } else {
                view.showUpdatedNotification();
                dataProvider.refreshItem(e);
            }
            close();
        });

    }

    private void open(ToDoItem task, boolean edit) {
        view.setDialogElementsVisibility(edit);
        view.setOpened(true);

        if (edit) {
            view.getOpenedTaskEditor().read(task, entityPresenter.isNew());
        } else {
            view.getOpenedTaskDetails().display(task, false);
        }
    }



    void back() {
        view.setDialogElementsVisibility(true);
    }
}
