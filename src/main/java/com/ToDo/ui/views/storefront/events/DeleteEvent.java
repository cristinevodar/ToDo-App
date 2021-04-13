package com.ToDo.ui.views.storefront.events;

import com.ToDo.ui.views.orderedit.OrderItemEditor;
import com.vaadin.flow.component.ComponentEvent;

public class DeleteEvent extends ComponentEvent<OrderItemEditor> {
	public DeleteEvent(OrderItemEditor component) {
		super(component, false);
	}
}