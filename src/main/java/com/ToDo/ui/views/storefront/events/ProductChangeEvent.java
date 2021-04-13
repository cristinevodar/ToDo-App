package com.ToDo.ui.views.storefront.events;

import com.ToDo.ui.views.orderedit.OrderItemEditor;
import com.vaadin.flow.component.ComponentEvent;
import com.ToDo.backend.data.entity.Product;

public class ProductChangeEvent extends ComponentEvent<OrderItemEditor> {

	private final Product product;

	public ProductChangeEvent(OrderItemEditor component, Product product) {
		super(component, false);
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

}