package com.ToDo.ui.utils.converters;

import static com.ToDo.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.ToDo.ui.utils.FormattingUtils;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class LocalTimeConverter implements ModelEncoder<LocalTime, String>, Converter<String, LocalTime> {

	@Override
	public String encode(LocalTime modelValue) {
		return convertIfNotNull(modelValue, FormattingUtils.HOUR_FORMATTER::format);
	}

	@Override
	public LocalTime decode(String presentationValue) {
		return convertIfNotNull(presentationValue, p -> LocalTime.parse(p, FormattingUtils.HOUR_FORMATTER));
	}

	@Override
	public Result<LocalTime> convertToModel(String value, ValueContext context) {
		try {
			return Result.ok(decode(value));
		} catch (DateTimeParseException e) {
			return Result.error("Invalid time");
		}
	}

	@Override
	public String convertToPresentation(LocalTime value, ValueContext context) {
		return encode(value);
	}

}
