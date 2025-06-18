package com.scu.Accommodation.mapper;

import com.scu.Accommodation.model.entity.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 帖子数据库操作测试
 *
 */
@SpringBootTest
class PostMapperTest {

    @Resource
    private PostMapper postMapper;

    @Test
    void listPostWithDelete() {
        List<Post> postList = postMapper.listPostWithDelete(new Date());
        Assertions.assertNotNull(postList);
    }

    @Test
    void batchInsert() {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setTitle("帖子1");
        post1.setContent("内容1");
        post1.setTags("标签1");
        post1.setThumbNum(10);
        post1.setFavourNum(100);
        post1.setUserId(1L);
        post1.setCreateTime(new Date());
        post1.setUpdateTime(new Date());
        post1.setIsDelete(0);
        Post post2 = new Post();
        post2.setTitle("帖子2");
        post2.setContent("内容2");
        post2.setTags("标签2");
        post2.setThumbNum(20);
        post2.setFavourNum(20);
        post2.setUserId(2L);
        post2.setCreateTime(new Date());
        post2.setUpdateTime(new Date());
        post2.setIsDelete(0);
        posts.add(post1);
        posts.add(post2);
        // 假设每个Post对象都有一个setId方法
        int affectedRows = postMapper.batchInsertPosts(posts);
        assertTrue(affectedRows > 0, "批量插入应影响至少一行");
    }

    @Test
    void batchDelete() {
        List<Long> postIds = new ArrayList<>();
        postIds.add(1L);
        postIds.add(2L);

        int affectedRows = postMapper.batchDeletePosts(postIds);
        assertTrue(affectedRows > 0, "批量删除应影响至少一行");
    }

}