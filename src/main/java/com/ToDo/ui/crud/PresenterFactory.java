/**
 *
 */
package com.ToDo.ui.crud;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.service.ToDoItemService;
import com.ToDo.ui.views.tasks.TasksView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.ToDo.app.security.CurrentUser;
import com.ToDo.backend.data.entity.Order;
import com.ToDo.backend.service.OrderService;
import com.ToDo.ui.views.storefront.StorefrontView;

@Configuration
public class PresenterFactory {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Order, StorefrontView> orderEntityPresenter(OrderService crudService, CurrentUser currentUser) {
		return new EntityPresenter<>(crudService, currentUser);
	}


	@Bean
	@Primary
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<ToDoItem, TasksView> taskEntityPresenter(ToDoItemService crudService, CurrentUser currentUser) {
		return new EntityPresenter<ToDoItem,TasksView>(crudService, currentUser);
	}


}
