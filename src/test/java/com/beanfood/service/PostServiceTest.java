package com.beanfood.service;

import com.beanfood.domain.Post;
import com.beanfood.repository.PostRepository;
import com.beanfood.request.PostCreate;
import com.beanfood.request.PostSearch;
import com.beanfood.response.PostResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        Post post = postRepository.findById(1L).get();

        assertEquals(1L, postRepository.count());
        assertEquals("제목 입니다.", post.getTitle());
        assertEquals("내용 입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(newPost);

        // when
        PostResponse post = postService.get(newPost.getId());

        // then
        assertNotNull(post);
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("이즈콩 제목 " + i)
                        .content("반포 자이 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("이즈콩 제목 30", posts.get(0).getTitle());
        assertEquals("이즈콩 제목 21", posts.get(9).getTitle());
    }

}