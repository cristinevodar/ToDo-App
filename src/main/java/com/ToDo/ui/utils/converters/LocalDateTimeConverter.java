package com.ToDo.ui.utils.converters;

import static com.ToDo.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalDateTime;

import com.ToDo.ui.utils.FormattingUtils;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class LocalDateTimeConverter implements ModelEncoder<LocalDateTime, String> {


	private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();

	@Override
	public String encode(LocalDateTime modelValue) {
		return convertIfNotNull(modelValue,
				v -> FormattingUtils.FULL_DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.encode(v.toLocalTime()));
	}

	@Override
	public LocalDateTime decode(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}
