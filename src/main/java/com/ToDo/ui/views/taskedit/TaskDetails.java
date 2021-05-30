package com.ToDo.ui.views.taskedit;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.ui.events.CancelEvent;
import com.ToDo.ui.events.SaveEvent;
import com.ToDo.ui.utils.converters.*;
import com.ToDo.ui.views.tasks.events.EditEvent;
import com.ToDo.ui.views.tasks.converters.TaskLocalDateConverter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.ToDo.ui.views.tasks.events.CommentEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;


@Tag("task-details")
@JsModule("./src/views/taskedit/task-details.js")
public class TaskDetails extends PolymerTemplate<TaskDetails.Model> {

    private ToDoItem toDoItem;

    @Id("back")
    private Button back;

    @Id("cancel")
    private Button cancel;

    @Id("save")
    private Button save;

    @Id("edit")
    private Button edit;



    private boolean isDirty;

    public TaskDetails(){

        save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        edit.addClickListener(e -> fireEvent(new EditEvent(this)));
    }

    public void display(ToDoItem toDoItem, boolean review) {
        getModel().setReview(review);
        this.toDoItem = toDoItem;
        getModel().setItem(toDoItem);

        this.isDirty = review;
    }

    public interface Model extends TemplateModel {
        @Include({ "id", "dueDate.day", "dueDate.weekday", "dueDate.date", "dueTime", "title",
                "description", "priority", "status", "users"})
        @Encode(value = LongToStringConverter.class, path = "id")
        @Encode(value = TaskLocalDateConverter.class, path = "dueDate")
        @Encode(value = LocalTimeConverter.class, path = "dueTime")
        @Encode(value = LocalDateTimeConverter.class, path = "history.timestamp")
        @Encode(value = StatusConverter.class, path = "status")
        @Encode(value = PriorityConverter.class, path = "priority")
        void setItem(ToDoItem toDoItem);

        void setReview(boolean review);
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public Registration addSaveListenter(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public Registration addBackListener(ComponentEventListener<ClickEvent<Button>> listener) {
        return back.addClickListener(listener);
    }

    public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
        return addListener(CommentEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}
