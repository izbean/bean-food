package com.beanfood.controller;

import com.beanfood.domain.Post;
import com.beanfood.repository.PostRepository;
import com.beanfood.request.PostCreate;
import com.beanfood.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 타이틀 입력은 필수다.")
    void test() throws Exception {
        // given
        PostCreate request = new PostCreate("", "내용 입니다.");



        // expected
        mvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못 된 요청 입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장 된다.")
    void test2() throws Exception {
        // given
        PostCreate request = new PostCreate("테스트", "내용 입니다.");

        ObjectMapper mapper = new ObjectMapper();

        // when
        mvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(print());


        // then
        Post post = postRepository.findById(1L).get();

        assertEquals(1L, postRepository.count());
        assertEquals("테스트", post.getTitle());
        assertEquals("내용 입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test3() throws Exception {
        // given
        Post post = Post.builder()
                .title("12345678901")
                .content("bar")
                .build();

        postRepository.save(post);

        // expected
        mvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test4() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("이즈콩 제목 " + i)
                        .content("반포 자이 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("이즈콩 제목 30"))
                .andExpect(jsonPath("$[0].content").value("반포 자이 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5() throws Exception {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이즈콩")
                .content("반포 자이")
                .build();

        // expected
        mvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(post))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        // expected
        mvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        // expected
        mvc.perform(delete("/posts/{postId}", 999L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test8() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("이즈콩")
                .content("반포 자이")
                .build();

        // expected
        mvc.perform(patch("/posts/{postId}", 999L)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함 될 수 없다.")
    void test9() throws Exception {
        // given
        PostCreate request = new PostCreate("바보", "내용 입니다.");

        // expected
        mvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}