package com.ToDo.ui.views.tasks.events;

import com.ToDo.ui.views.taskedit.TaskDetails;
import com.vaadin.flow.component.ComponentEvent;

public class CommentEvent extends ComponentEvent<TaskDetails> {
    private Long toDoItemId;
    private String message;

    public CommentEvent(TaskDetails component, Long toDoItemId, String message) {
        super(component, false);
        this.toDoItemId = toDoItemId;
        this.message = message;
    }

    public Long getToDoItemId() {
        return toDoItemId;
    }

    public String getMessage() {
        return message;
    }
}
