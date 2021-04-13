package com.ToDo.ui.views.storefront.events;

import com.ToDo.ui.views.orderedit.OrderDetails;
import com.vaadin.flow.component.ComponentEvent;

public class CommentEvent extends ComponentEvent<OrderDetails> {

	private Long orderId;
	private String message;

	public CommentEvent(OrderDetails component, Long orderId, String message) {
		super(component, false);
		this.orderId = orderId;
		this.message = message;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getMessage() {
		return message;
	}
}