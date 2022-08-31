package com.beanfood.repository;

import com.beanfood.domain.Post;
import com.beanfood.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.beanfood.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .orderBy(post.id.desc())
                .limit(10)
                .offset(postSearch.getOffset())
                .fetch();
    }

}
