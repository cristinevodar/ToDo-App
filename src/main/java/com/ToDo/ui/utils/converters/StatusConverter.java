package com.ToDo.ui.utils.converters;

import com.ToDo.backend.data.entity.Status;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ToDo.ui.dataproviders.DataProviderUtil.convertIfNotNull;

public class StatusConverter implements ModelEncoder<Status, String> {

    private Map<String, Status> values;

    public StatusConverter() {
        values = Arrays.stream(Status.values())
                .collect(Collectors.toMap(Status::toString, Function.identity()));
    }

    @Override
    public Status decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }



    @Override
    public String encode(Status modelValue) {
        return convertIfNotNull(modelValue, Status::toString);
    }

}
