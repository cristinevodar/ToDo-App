package com.ToDo.app.security;

import com.ToDo.backend.data.entity.UserSession;
import com.ToDo.ui.utils.BakeryConst;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ToDo.backend.data.Role;
import com.ToDo.backend.data.entity.User;
import com.ToDo.backend.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the {@link UserDetailsServiceImpl}.</li>

 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_URL = "/" + BakeryConst.PAGE_STOREFRONT;
	private static final String LOGOUT_SUCCESS_URL = "/" + BakeryConst.PAGE_STOREFRONT;

	private final UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserSession userSession;

	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CurrentUser currentUser(UserRepository userRepository) {
		if(userRepository.findByEmailIgnoreCase(userSession.getUser().getEmail())==null) {
			User user2 = userSession.getUser();
			user2.setPasswordHash("alalalal");
			user2.setRole("admin");
			userRepository.save(user2);
		}
		User user = userRepository.findByEmailIgnoreCase(userSession.getUser().getEmail()) ;

		return () -> user;
	}
//
//	/**
//	 * Registers our UserDetailsService and the password encoder to be used on login attempts.
//	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		super.configure(auth);
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//	}
//
//	/**
//	 * Require login to access internal pages and configure login form.
//	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// Not using Spring CSRF here to be able to use plain HTML for the login page
//		http.csrf().disable()
//
//				// Register our CustomRequestCache, that saves unauthorized access attempts, so
//				// the user is redirected after login.
//				.requestCache().requestCache(new CustomRequestCache())
//
//				// Restrict access to our application.
//				.and().authorizeRequests()
//
//				// Allow all flow internal requests.
//				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
//
//				// Allow all requests by logged in users.
//				.anyRequest().hasAnyAuthority(Role.getAllRoles())
//
//				// Configure the login page.
//				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
//				.failureUrl(LOGIN_FAILURE_URL)
//
//				// Register the success handler that redirects users to the page they last tried
//				// to access
//				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
//
//				// Configure logout
//				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
//	}
//
//	/**
//	 * Allows access to static resources, bypassing Spring security.
//	 */
//	@Override
//	public void configure(WebSecurity web) {
//		web.ignoring().antMatchers(
//				// client-side JS code
//				"/VAADIN/**",
//
//				// the standard favicon URI
//				"/favicon.ico",
//
//				// the robots exclusion standard
//				"/robots.txt",
//
//				// web application manifest
//				"/manifest.webmanifest",
//				"/sw.js",
//				"/offline-page.html",
//
//				// icons and images
//				"/icons/**",
//				"/images/**",
//
//				// (development mode) H2 debugging console
//				"/h2-console/**"
//		);
//	}


		@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Registers our UserDetailsService and the password encoder to be used on login
	 * attempts.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable()

				// Allow all flow internal requests.
				.authorizeRequests().requestMatchers(SecurityConfiguration::isFrameworkInternalRequest).permitAll()

				// Restrict access to our application.
				.and().authorizeRequests().anyRequest().authenticated()

				// Not using Spring CSRF here to be able to use plain HTML for the login page
				.and().csrf().disable()

				// Configure logout
				.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)

				// Configure the login page with OAuth.
				.and().oauth2Login().loginPage(LOGIN_URL).permitAll();
		// @formatter:on
	}

	/**
	 * Allows access to static resources, bypassing Spring Security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// client-side JS code
				"/VAADIN/**",

				// client-side JS code
				"/frontend/**",

				// the standard favicon URI
				"/favicon.ico",

				// web application manifest
				"/manifest.webmanifest", "/sw.js", "/offline-page.html",

				// icons and images
				"/icons/**", "/images/**");
	}

	/**
	 * Tests if the request is an internal framework request. The test consists of
	 * checking if the request parameter is present and if its value is consistent
	 * with any of the request types know.
	 *
	 * @param request {@link HttpServletRequest}
	 * @return true if is an internal framework request. False otherwise.
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}
}
