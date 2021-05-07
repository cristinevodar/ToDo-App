package com.ToDo.ui.views.admin.products;

import com.ToDo.ui.utils.BakeryConst;
import com.ToDo.ui.utils.converters.CurrencyFormatter;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.ToDo.app.security.CurrentUser;
import com.ToDo.backend.data.Role;
import com.ToDo.backend.data.entity.Product;
import com.ToDo.backend.service.ProductService;
import com.ToDo.ui.MainView;
import com.ToDo.ui.crud.AbstractBakeryCrudView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Currency;

@Route(value = BakeryConst.PAGE_PRODUCTS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)

public class ProductsView extends AbstractBakeryCrudView<Product> {

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	@Autowired
	public ProductsView(ProductService service, CurrentUser currentUser) {
		super(Product.class, service, new Grid<>(), createForm(), currentUser);
	}

	@Override
	protected void setupGrid(Grid<Product> grid) {
		grid.addColumn(Product::getName).setHeader("Product Name").setFlexGrow(10);
		grid.addColumn(p -> currencyFormatter.encode(p.getPrice())).setHeader("Unit Price");
	}

	@Override
	protected String getBasePage() {
		return BakeryConst.PAGE_PRODUCTS;
	}

	private static BinderCrudEditor<Product> createForm() {
		TextField name = new TextField("Product name");
		name.getElement().setAttribute("colspan", "2");
		TextField price = new TextField("Unit price");
		price.getElement().setAttribute("colspan", "2");

		FormLayout form = new FormLayout(name, price);

		BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

		binder.bind(name, "name");

		binder.forField(price).withConverter(new PriceConverter()).bind("price");
		price.setPattern("\\d+(\\.\\d?\\d?)?$");
		price.setPreventInvalidInput(true);

		String currencySymbol = Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol();
		price.setPrefixComponent(new Span(currencySymbol));

		return new BinderCrudEditor<Product>(binder, form);
	}

}
