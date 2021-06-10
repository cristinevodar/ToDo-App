package com.ToDo.ui.dataproviders;

import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.service.ToDoItemService;
import com.ToDo.ui.utils.TasksConst;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SpringComponent
@UIScope
public class TasksGridDataProvider extends FilterablePageableDataProvider<ToDoItem, TasksGridDataProvider.TaskFilter> {


    public static class TaskFilter implements Serializable {
        private String filter;
        private boolean showPrevious;

        public String getFilter() {
            return filter;
        }

        public boolean isShowPrevious() {
            return showPrevious;
        }

        public TaskFilter(String filter, boolean showPrevious) {
            this.filter = filter;
            this.showPrevious = showPrevious;
        }


        public static TaskFilter getEmptyFilter() {
            return new TaskFilter("", false);
        }
    }


    private final ToDoItemService toDoItemService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<ToDoItem>> pageObserver;

    @Autowired
    public TasksGridDataProvider(ToDoItemService toDoItemService) {
        this.toDoItemService = toDoItemService;
        setSortOrders(TasksConst.DEFAULT_SORT_DIRECTION, TasksConst.ORDER_SORT_FIELDS);
    }


    private void setSortOrders(Sort.Direction direction, String[] properties) {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        for (String property : properties) {
            if (direction.isAscending()) {
                builder.thenAsc(property);
            } else {
                builder.thenDesc(property);
            }
        }
        defaultSortOrders = builder.build();
    }

    @Override
    protected Page<ToDoItem> fetchFromBackEnd(Query<ToDoItem, TaskFilter> query, Pageable pageable) {
        TaskFilter filter = query.getFilter().orElse(TaskFilter.getEmptyFilter());
        Page<ToDoItem> page = toDoItemService.findAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()),
                getFilterDate(filter.isShowPrevious()), pageable);
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        return page;
    }

    private Optional<LocalDate> getFilterDate(boolean showPrevious) {
        if (showPrevious) {
            return Optional.empty();
        }

        return Optional.of(LocalDate.now().minusDays(1));
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<ToDoItem, TaskFilter> query) {
        TaskFilter filter = query.getFilter().orElse(TaskFilter.getEmptyFilter());
        return (int) toDoItemService
                .countAnyMatchingAfterDueDate(Optional.ofNullable(filter.getFilter()), getFilterDate(filter.isShowPrevious()));
    }

    public void setPageObserver(Consumer<Page<ToDoItem>> pageObserver) {
        this.pageObserver = pageObserver;
    }

    @Override
    public Object getId(ToDoItem item) {
        return item.getId();
    }


}
