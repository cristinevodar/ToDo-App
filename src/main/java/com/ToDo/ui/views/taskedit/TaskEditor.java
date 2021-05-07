package com.ToDo.ui.views.taskedit;


import com.ToDo.backend.data.entity.Status;
import com.ToDo.backend.data.entity.ToDoItem;
import com.ToDo.backend.data.entity.User;
import com.ToDo.ui.dataproviders.DataProviderUtil;
import com.ToDo.ui.events.CancelEvent;
import com.ToDo.ui.utils.converters.LocalTimeConverter;
import com.ToDo.ui.views.tasks.events.ReviewEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.ToDo.ui.dataproviders.DataProviderUtil.createItemLabelGenerator;

@Tag("task-editor")
@JsModule("./src/views/taskedit/task-editor.js")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskEditor extends PolymerTemplate<TaskEditor.Model> {


    public interface Model extends TemplateModel {
        void setState(String status);
    }

    @Id("taskTitle")
    private H2 taskTitle;

    @Id("metaContainer")
    private Div metaContainer;

    @Id("taskNumber")
    private Span taskNumber;

    @Id("title")
    private TextField title;

    @Id("description")
    private TextField description;

    @Id("priority")
    private TextField priority;


    @Id("status")
    private ComboBox<Status> status;

    @Id("dueDate")
    private DatePicker dueDate;

    @Id("dueTime")
    private ComboBox<LocalTime> dueTime;

    @Id("cancel")
    private Button cancel;

    @Id("review")
    private Button review;



    private User currentUser;

    private BeanValidationBinder<ToDoItem> binder = new BeanValidationBinder<>(ToDoItem.class);

    private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();


    @Autowired
    public TaskEditor() {
        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        review.addClickListener(e -> fireEvent(new ReviewEvent(this)));
        status.setItemLabelGenerator(createItemLabelGenerator(Status::getDisplayName));

        status.setDataProvider(DataProvider.ofItems(Status.values()));
        status.addValueChangeListener(
                e -> getModel().setState(DataProviderUtil.convertIfNotNull(e.getValue(), Status::name)));
        binder.forField(status)
                .withValidator(new BeanValidator(ToDoItem.class, "status"))//TODO sau state?
                .bind(ToDoItem::getStatus, (o, s) -> {
                    o.changeStatus(currentUser, s);
                });

        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");

        SortedSet<LocalTime> timeValues = IntStream.rangeClosed(8, 16).mapToObj(i -> LocalTime.of(i, 0))
                .collect(Collectors.toCollection(TreeSet::new));
        dueTime.setItems(timeValues);
        dueTime.setItemLabelGenerator(localTimeConverter::encode);
        binder.bind(dueTime, "dueTime");

        title.setRequired(true);
        binder.bind(title,"title");

        description.setRequired(false);
        binder.bind(description,"description");

        title.setRequired(true);
        binder.bind(priority,"priority");

        binder.addValueChangeListener(e -> {
            if (e.getOldValue() != null) {
                review.setEnabled(hasChanges());
            }
        });
    }

    public boolean hasChanges() {
        return binder.hasChanges() ;
    }

    public void clear() {
        binder.readBean(null);

    }

    public void close() {

    }

    public void write(ToDoItem toDoItem) throws ValidationException {
        binder.writeBean(toDoItem);
    }

    public void read(ToDoItem toDoItem, boolean isNew) {
        binder.readBean(toDoItem);

        this.taskNumber.setText(isNew ? "" : toDoItem.getId().toString());
        title.setVisible(isNew);
        metaContainer.setVisible(!isNew);

        if (toDoItem.getStatus() != null) {
            getModel().setState(toDoItem.getStatus().name());
        }

        review.setEnabled(false);
    }

    public Stream<HasValue<?, ?>> validate() {
        Stream<HasValue<?, ?>> errorFields = binder.validate().getFieldValidationErrors().stream()
                .map(BindingValidationStatus::getField);

        return errorFields;
    }

    public Registration addReviewListener(ComponentEventListener<ReviewEvent> listener) {
        return addListener(ReviewEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

}

