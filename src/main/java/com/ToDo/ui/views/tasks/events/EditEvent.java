package com.ToDo.ui.views.tasks.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class EditEvent extends ComponentEvent<Component> {

	public EditEvent(Component source) {
		super(source, false);
	}
}
