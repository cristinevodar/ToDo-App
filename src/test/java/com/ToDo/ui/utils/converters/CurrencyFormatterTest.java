/**
 *
 */
package com.ToDo.ui.utils.converters;

import static org.junit.Assert.assertEquals;

import com.ToDo.test.FormattingTest;
import org.junit.Test;

public class CurrencyFormatterTest extends FormattingTest {

	@Test
	public void formattingShoudBeLocaleIndependent() {
		CurrencyFormatter formatter = new CurrencyFormatter();
		String result = formatter.encode(123456);
		assertEquals("$1,234.56", result);
	}
}
