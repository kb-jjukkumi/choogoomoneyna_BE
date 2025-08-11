package com.choogoomoneyna.choogoomoneyna_be.dockertest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestHomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/api/test/ping";  // 또는 적절한 경로
    }
}
