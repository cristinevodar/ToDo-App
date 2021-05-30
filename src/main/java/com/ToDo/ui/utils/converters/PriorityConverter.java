package com.ToDo.ui.utils.converters;

import com.ToDo.backend.data.entity.Priority;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ToDo.ui.dataproviders.DataProviderUtil.convertIfNotNull;

public class PriorityConverter implements ModelEncoder<Priority, String> {

    private Map<String, Priority> values;

    public PriorityConverter() {
        values = Arrays.stream(Priority.values())
                .collect(Collectors.toMap(Priority::toString, Function.identity()));
    }

    @Override
    public Priority decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }



    @Override
    public String encode(Priority modelValue) {
        return convertIfNotNull(modelValue, Priority::toString);
    }
}
