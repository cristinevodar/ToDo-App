package com.ToDo.ui.views.login;

import com.ToDo.app.security.SecurityUtils;
import com.ToDo.ui.utils.BakeryConst;
import com.ToDo.ui.views.storefront.StorefrontView;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Route
@PageTitle("ToDo App")
@JsModule("./styles/shared-styles.js")
@Viewport(BakeryConst.VIEWPORT)
public class LoginScreen extends VerticalLayout
	implements AfterNavigationObserver, BeforeEnterObserver {


    private static final String URL = "/oauth2/authorization/google";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientkey;

    public LoginScreen() {

        setPadding(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

    @PostConstruct
    public void initView() {

        // Check that oauth keys are present
        if (clientkey == null || clientkey.isEmpty() || clientkey.length() < 16) {
            Paragraph text = new Paragraph("Could not find OAuth client key in application.properties. "
                    + "Please double-check the key and refer to the README.md file for instructions.");
            text.getStyle().set("padding-top", "100px");
            add(text);

        } else {

            Anchor gplusLoginButton = new Anchor(URL, "Login with Google");
            gplusLoginButton.getStyle().set("margin-top", "100px");
            add(gplusLoginButton);
        }

    }
    	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {

			event.forwardTo(StorefrontView.class);
		}
	}

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}