package com.wangyi.service;

import com.wangyi.Dao.*;
import com.wangyi.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ForumService  {
    private TopicDao topicDao ;
    private UserDao userDao ;
    private BoardDao boardDao ;
    private PostDao postDao ;

    @Autowired
    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setBoardDao(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    /**
     * 发表一个主题帖子,用户积分+10,论坛板块主题贴数+1
     * @param topic
     */
    public void addTopic(Topic topic){
        Board board = (Board) boardDao.get(topic.getBoardId());   //根据主题的板块id获取板块
        board.setTopicNum(board.getTopicNum()+1);                 //板块的主题数量+1
        topicDao.save(topic);

        topic.getMainPost().setTopic(topic);
        MainPost post = topic.getMainPost();
        post.setCreateTime(new Date());
        post.setUser(topic.getUser());
        post.setPostTitle(topic.getTopicTitle());
        post.setBoardId(topic.getBoardId());
        postDao.save(topic.getMainPost());

        User user = topic.getUser();
        user.setCredit(user.getCredit()+10);
        userDao.update(user);
    }

    /**
     * 删除一个主题贴,用户积分减50,论坛板块主题帖数减一,删除
     * 主题帖所有关联的帖子
     */
    public void removeTopic(int topicId){
        Topic topic = topicDao.get(topicId) ;

        //将论坛板块主题帖数减一
        Board board = boardDao.get(topic.getBoardId());
        board.setTopicNum(board.getTopicNum()-1);

        //
        User user = topic.getUser();
        user.setCredit(user.getCredit()-50);

        //
        topicDao.remove(topic);
        postDao.deleteTopicPosts(topicId);
    }

    public void addPost(Post post){
        postDao.save(post);

        User user = post.getUser();
        user.setCredit(user.getCredit()+5);
        userDao.update(user);

        Topic topic = topicDao.get(post.getTopic().getTopicId()) ;
        topic.setReplies(topic.getReplies()+1);
        topic.setLastPost(new Date());
        //topic处于hibernate的受管状态,不需要更新

    }

    public void removePost(int postId){
        Post post = postDao.get(postId);
        postDao.remove(post);

        Topic topic = topicDao.get(post.getTopic().getTopicId()) ;
        topic.setReplies(topic.getReplies() -1 );

        User user = post.getUser();
        user.setCredit(user.getCredit() - 20);
    }

    public void addBoard(Board board){
        boardDao.save(board);
    }

    public void removeBoard(int boardId){
        Board board = boardDao.get(boardId) ;
        boardDao.remove(board);
    }

    public void makeDigestTopic(int topicId){
        Topic topic = topicDao.get(topicId);
        topic.setDigest(Topic.DIGEST_TOPIC);
        User user = topic.getUser();
        user.setCredit(user.getCredit()+100);
    }

    public List<Board> getAllBoards(){
        return boardDao.loadAll();
    }

    public Page getPagedTopic(int boardId ,int pageNo,int pageSize){
        return topicDao.getPagedTopics(boardId,pageNo,pageSize) ;
    }

    public Page getPagePosts(int topicId,int pageNo,int pageSize){
        return postDao.getPagedPosts(topicId,pageNo,pageSize) ;
    }

    public Page queryTopicByTitle(String title ,int pageNo,int pageSize){
        return topicDao.queryTopicByTitle(title,pageNo,pageSize);
    }

    public Board getBoardById(int boardId){
        return boardDao.get(boardId);
    }

    public Topic getTopicByTopicId(int topicId){
        return topicDao.get(topicId) ;
    }

    public Post getPostByPostId(int postId){
        return postDao.get(postId);
    }

    public void addBoardManager(int boardId,String userName){
        User user = userDao.getUserByUserName(userName);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }else{
            Board board = boardDao.get(boardId);
            user.getManBoards().add(board) ;
            userDao.update(user);
        }

    }

    public void updateTopic(Topic topic){
        topicDao.update(topic);
    }

    public void updatePost(Post post){
        postDao.update(post);
    }
}
