package com.ToDo.ui.views.about;

import com.ToDo.ui.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Version;

import java.util.ResourceBundle;

@Route(value = "About", layout = MainView.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {

    //private transient ResourceBundle resourceBundle = ResourceBundle.getBundle("MockDataWords", UI.getCurrent().getLocale());

    public AboutView() {
        add(VaadinIcon.INFO_CIRCLE.create());
        add(new Html(
                ("<p>A responsive application template with some dummy data. Loosely based on " +
                        "the <b>responsive layout grid</b> guidelines set by " +
                        "<a href=\"http://localhost:8080/tasks\">Material Design</a>. " +
                        "Utilises the <a href=\"https://vaadin.com/themes/lumo\">Lumo</a> theme.</p>")));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);
    }
}