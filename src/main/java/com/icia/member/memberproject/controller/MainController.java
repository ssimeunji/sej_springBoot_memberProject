package com.icia.member.memberproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @GetMapping(value="/")
    public String index() {
        return "index";
    }
}
