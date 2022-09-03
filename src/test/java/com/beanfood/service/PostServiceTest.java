package com.beanfood.service;

import com.beanfood.domain.Post;
import com.beanfood.exception.PostNotFound;
import com.beanfood.repository.PostRepository;
import com.beanfood.request.PostCreate;
import com.beanfood.request.PostEdit;
import com.beanfood.request.PostSearch;
import com.beanfood.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
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
        Long id = postService.write(postCreate);

        // then
        Post post = postRepository.findById(id).get();

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

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이즈풀")
                .content("반포 자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("이즈풀", changedPost.getTitle());
        assertEquals("반포 자이", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이즈콩 제목")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertEquals("이즈콩 제목", changedPost.getTitle());
        assertEquals("초가집", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test7() {
        // given
        Post newPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(newPost);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(newPost.getId() + 1);
        }, "예외처리가 잘못 되었어요.");
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이즈콩 제목")
                .content("초가집")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1, postEdit);
        }, "예외처리가 잘못 되었어요.");
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("이즈콩 제목")
                .content("반포 자이")
                .build();

        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1);
        }, "예외처리가 잘못 되었어요.");
    }

}