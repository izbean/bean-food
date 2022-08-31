package com.beanfood.repository;

import com.beanfood.domain.Post;
import com.beanfood.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
