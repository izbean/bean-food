package com.beanfood.controller;

import com.beanfood.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params) {
        return Map.of();
    }


}
