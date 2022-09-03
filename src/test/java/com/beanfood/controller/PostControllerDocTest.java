package com.beanfood.controller;

import com.beanfood.domain.Post;
import com.beanfood.repository.PostRepository;
import com.beanfood.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.beanfood.com", uriPort = 443)
@ExtendWith({RestDocumentationExtension.class})
public class PostControllerDocTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("아무개")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", post.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("post-inquiry",
                        pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("title").description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {
        // given
        PostCreate request = new PostCreate("테스트", "내용 입니다.");

        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("post-create",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("제목")
                                        .attributes(Attributes.key("constraint").value("좋은 제목 입력해주세요.")),
                                PayloadDocumentation.fieldWithPath("content").description("내용").optional()
                        )
                ));
    }

}
