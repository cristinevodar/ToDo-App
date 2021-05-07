package com.ToDo.ui.views.tasks.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;

import java.time.LocalDate;

import static com.ToDo.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.ToDo.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;

public class TaskLocalDateConverter implements ModelEncoder<LocalDate, TaskDate> {


    @Override
    public TaskDate encode(LocalDate modelValue) {
        TaskDate result= null;
        if(modelValue!=null){
            result=new TaskDate();
            result.setDay(MONTH_AND_DAY_FORMATTER.format(modelValue));
            result.setWeekday(WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
            result.setDate(modelValue.toString());
        }
        return result;
    }

    @Override
    public LocalDate decode(TaskDate taskDate) {
        throw new UnsupportedOperationException();
    }
}
