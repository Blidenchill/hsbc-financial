package com.hsbc.financial.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author zhaoyongping
 * @date 2025/1/10
 * @className TestController
 **/
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("ok")
    public String test() {
        return "OK";
    }
}
