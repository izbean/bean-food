package com.beanfood.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("/posts 요청시 타이틀 입력은 필수다.")
    void test() throws Exception {
        // expected
        mvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"내용 입니다.\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력 해 주세요."))
                .andDo(print());
    }

}