package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by wcc on 2019\5\21 0021 13:00
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("index.page")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }
}
