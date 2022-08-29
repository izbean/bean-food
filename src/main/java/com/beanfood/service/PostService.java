package com.beanfood.service;

import com.beanfood.domain.Post;
import com.beanfood.repository.PostRepository;
import com.beanfood.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = new Post();
    }

}
