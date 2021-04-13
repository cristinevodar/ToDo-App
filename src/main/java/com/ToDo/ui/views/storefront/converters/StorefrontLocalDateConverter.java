package com.ToDo.ui.views.storefront.converters;

import com.ToDo.ui.utils.FormattingUtils;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.time.LocalDate;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class StorefrontLocalDateConverter implements ModelEncoder<LocalDate, StorefrontDate> {

	@Override
	public StorefrontDate encode(LocalDate modelValue) {
		StorefrontDate result = null;
		if (modelValue != null) {
			result = new StorefrontDate();
			result.setDay(FormattingUtils.MONTH_AND_DAY_FORMATTER.format(modelValue));
			result.setWeekday(FormattingUtils.WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
			result.setDate(modelValue.toString());
		}
		return result;
	}

	@Override
	public LocalDate decode(StorefrontDate storefrontDate) {
		throw new UnsupportedOperationException();
	}
}
