package com.rutsubosuki.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping("/")
    private ModelAndView index(
            @AuthenticationPrincipal UserDetails userDetails,
            ModelAndView mav
    ) {
        if (userDetails != null) {
            mav.addObject("username", userDetails.getUsername());
        }
        mav.setViewName("index");

        return mav;
    }
}