package com.ToDo.ui.utils.converters;

import com.ToDo.ui.utils.FormattingUtils;
import com.vaadin.flow.templatemodel.ModelEncoder;
import com.ToDo.ui.dataproviders.DataProviderUtil;

public class CurrencyFormatter implements ModelEncoder<Integer, String> {

	@Override
	public String encode(Integer modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
	}

	@Override
	public Integer decode(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}
