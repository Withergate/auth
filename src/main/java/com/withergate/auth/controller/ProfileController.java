package com.withergate.auth.controller;

import com.withergate.auth.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * ProfileController class.
 * </p>
 *
 * @author Martin Myslik
 */
@Controller
public class ProfileController {

    @Autowired
    private AuthProperties properties;

    /**
     * <p>
     * Return profile page.
     * </p>
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("gameUrl", properties.getGameUrl());
        return "profile";
    }

}
