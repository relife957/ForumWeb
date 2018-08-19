package com.wangyi.Dao;

import com.wangyi.domain.Post;
import org.springframework.stereotype.Repository;

@Repository
public class PostDao extends BaseDao<Post> {
    private static final String GET_PAGED_POSTS = "from Post where topic.topicId = ? order by createTime desc" ;

    private static final String DELETE_TOPIC_POSTS = "delete from Post where topic.topicId=?" ;

    public Page getPagedPosts(int topicId,int pageNo,int pageSize){
        return pagedQuery(GET_PAGED_POSTS,pageNo,pageSize,topicId);
    }

    public void deleteTopicPosts(int topicId){
        getHibernateTemplate().bulkUpdate(DELETE_TOPIC_POSTS,topicId);
    }
}
