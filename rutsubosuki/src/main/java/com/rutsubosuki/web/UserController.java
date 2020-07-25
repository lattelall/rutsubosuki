package com.rutsubosuki.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rutsubosuki.web.form.UserForm;

@Controller
public class UserController {

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/login*")
    private ModelAndView login(
            @ModelAttribute("username") String username,
            ModelAndView mav
    ) {
        mav.setViewName("login");

        return mav;
    }

    @RequestMapping("/signup")
    private ModelAndView signup(
            @ModelAttribute UserForm form,
            ModelAndView mav
    ) {
        mav.setViewName("signup");

        return mav;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    private String register(
            @Validated UserForm form,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return "signup";
        }
        if (userDetailsManager.userExists(form.getUsername())) {
            result.rejectValue("username", "user.exists");
            return "signup";
        }

        UserDetails user = new User(
            form.getUsername(),
            passwordEncoder.encode(form.getPassword()),
            AuthorityUtils.createAuthorityList("ROLE_USER")
        );
        userDetailsManager.createUser(user);

        return "redirect:/login?complete";
    }
}