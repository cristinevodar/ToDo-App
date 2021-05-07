package com.ToDo.ui.views.tasks.events;

import com.ToDo.ui.views.taskedit.TaskEditor;
import com.vaadin.flow.component.ComponentEvent;

public class ReviewEvent extends ComponentEvent<TaskEditor> {

    public ReviewEvent(TaskEditor component) {
        super(component, false);
    }
}
