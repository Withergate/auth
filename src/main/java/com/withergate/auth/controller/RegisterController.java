package com.withergate.auth.controller;

import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import com.withergate.auth.model.User;
import com.withergate.auth.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * <p>
 * RegistrationController class.
 * </p>
 *
 * @author Martin Myslik
 */
@Slf4j
@Controller
public class RegisterController {

    private static final String CONFIRM = "confirm";
    private static final String REGISTER = "register";

    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String CONFIRMATION_MESSAGE = "confirmationMessage";
    private static final String ERROR_MESSAGE = "errorMessage";

    private static final String INVALID_TOKEN ="invalidToken";
    private static final String CONFIRMATION_TOKEN ="confirmationToken";

    private final AccountService accountService;
    private final MessageSource messages;

    /**
     * <p>
     * Constructor for RegisterController.
     * </p>
     */
    public RegisterController(AccountService accountService, MessageSource messages) {
        this.accountService = accountService;
        this.messages = messages;
    }

    /**
     * <p>
     * Return ModelAndView for registration page.
     * </p>
     */
    @GetMapping("/register")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName(REGISTER);

        return modelAndView;
    }

    /**
     * <p>
     * Process input data from registration form and validate them.
     * </p>
     */
    @PostMapping("/register")
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, Locale locale) {
        log.debug("User registration - POST");

        modelAndView.setViewName(REGISTER);

        if (accountService.isUserRegistered(user)) {
            log.warn("This user already exists: {}", user);

            modelAndView.addObject(ERROR_MESSAGE, messages.getMessage("registration.emailExists", new Object[] {user.getEmail()}, locale));

            return modelAndView;
        }

        // new user so we create user and send confirmation e-mail
        accountService.registerUser(user, locale);

        modelAndView.addObject(CONFIRMATION_MESSAGE,
                messages.getMessage("registration.confirmationEmail", new Object[] {user.getEmail()}, locale));

        return modelAndView;
    }

    /**
     * <p>
     * Return ModelAndView for confirmation page.
     * </p>
     */
    @GetMapping("/confirm")
    public ModelAndView showConfirmationPage(
            ModelAndView modelAndView,
            @RequestParam("token") String token,
            @RequestParam(value = "mobile", required = false, defaultValue = "false") boolean mobile,
            @RequestParam(value = "passwordError", required = false) boolean passwordError,
            Locale locale
    ) {

        log.debug("Confirm endpoint - GET");

        modelAndView.setViewName(CONFIRM);

        if (passwordError) {
            log.debug("Passwords are not matching!");

            modelAndView.addObject(ERROR_MESSAGE, messages.getMessage("password.notMatching", null, locale));
        }

        Optional<User> optionalUser = accountService.getUserForToken(token);

        if (!optionalUser.isPresent()) { // No token found in DB
            log.debug("No user found for this token: {}", token);

            modelAndView.addObject(INVALID_TOKEN, messages.getMessage("registration.invalidToken", null, locale));
        } else { // Token found
            modelAndView.addObject(CONFIRMATION_TOKEN, optionalUser.get().getConfirmationToken());
        }

        // mobile param to model and view
        modelAndView.addObject("mobile", mobile);

        return modelAndView;
    }

    /**
     * <p>
     * Return Redirection to confirm page. This endpoint is used for mobile page
     * </p>
     */
    @GetMapping("/confirmRedirect")
    public String showRedirectConfirmationPage(@RequestParam("token") String token) {

        return "redirect:/confirm?token=" + token;
    }

    /**
     * <p>
     * Process input data from confirmation page.
     * </p>
     */
    @PostMapping(value = "/confirm")
    public ModelAndView processConfirmationForm(
            ModelAndView modelAndView,
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Locale locale
    ) {
        log.debug("Confirm endpoint - POST");

        modelAndView.setViewName(CONFIRM);

        if (!password.equals(confirmPassword)) {
            modelAndView.setView(new RedirectView(CONFIRM));
            modelAndView.addObject("token", token);
            modelAndView.addObject("passwordError", true);

            return modelAndView;
        }

        modelAndView.addObject("loginLink", "/login");

        accountService.confirmUser(token, password);

        modelAndView.addObject(SUCCESS_MESSAGE, messages.getMessage("registration.passwordSuccess", null, locale));
        return modelAndView;
    }

}
