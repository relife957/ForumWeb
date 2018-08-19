package com.wangyi.web;


import com.wangyi.Dao.Page;
import com.wangyi.cons.CommonConstant;
import com.wangyi.domain.Board;
import com.wangyi.domain.Post;
import com.wangyi.domain.Topic;
import com.wangyi.domain.User;
import com.wangyi.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class BoardManageController extends BaseController {
    private ForumService forumService ;

    @Autowired
    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }

    /**
     * 列出所有板块主题
     * @param boardId
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/board/listBoardTopics-{boardId}",method = RequestMethod.GET)
    public ModelAndView listBoardTopics(@PathVariable Integer boardId, @RequestParam(value = "pageNo",required=false)Integer pageNo){
        ModelAndView view = new ModelAndView() ;
        Board board = forumService.getBoardById(boardId);
        pageNo = pageNo==null?1:pageNo ;
        Page pagedTopic = forumService.getPagedTopic(boardId,pageNo,CommonConstant.PAGE_SIZE) ;
        view.addObject("board",board);
        view.addObject("pagedTopic",pagedTopic);
        view.setViewName("/listBoardTopics");
        return view ;
    }

    /**
     * 添加主题贴页面
     * @param boardId
     * @return
     */
    @RequestMapping(value = "/board/addTopicPage-{boardId}",method = RequestMethod.GET)
    public ModelAndView addTopicPage(@PathVariable Integer boardId){
        ModelAndView view = new ModelAndView() ;
        view.addObject("boardId",boardId);
        view.setViewName("/addTopic");
        return view ;
    }

    /**
     * 添加一个主题贴
     * @param request
     * @param topic
     * @return
     */
    @RequestMapping(value = "/board/addTopic" , method = RequestMethod.POST)
    public String addTopic(HttpServletRequest request, Topic topic){
        User user = getSessionUser(request);
        topic.setUser(user);
        Date now = new Date() ;
        topic.setCreateTime(now);
        topic.setLastPost(now);
        forumService.addTopic(topic);
        String targetUrl = "/board/listBoardTopics-"+topic.getBoardId()+".html";
        return "redirect:"+targetUrl ;
    }


    /**
     * 列出主题所有帖子
     * @param topicId
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "/board/listTopicPosts-{topicId}",method = RequestMethod.GET)
    public ModelAndView listTopicPosts(@PathVariable Integer topicId,@RequestParam(value = "pageNo",required = false)Integer pageNo){
        ModelAndView view = new ModelAndView();
        Topic topic = forumService.getTopicByTopicId(topicId);
        pageNo = pageNo == null? 1:pageNo ;
        Page pagedPost = forumService.getPagePosts(topicId,pageNo,CommonConstant.PAGE_SIZE);

        view.addObject("topic",topic);
        view.addObject("pagedPost",pagedPost);
        view.setViewName("/listTopicPosts");
        return view ;
    }


    /**
     * 回复主题
     * @param request
     * @param post
     * @return
     */
    @RequestMapping(value = "/board/addPost")
    public String addPost(HttpServletRequest request,Post post){
        post.setCreateTime(new Date());
        post.setUser(getSessionUser(request));
        Topic topic = new Topic() ;
        int topicId = Integer.valueOf(request.getParameter("topicId"));
        if(topicId > 0){
            topic.setTopicId(topicId);
            post.setTopic(topic);
        }
        forumService.addPost(post);
        String targetUrl = "/board/listTopicPosts-"+post.getTopic().getTopicId()+".html" ;
        return "redirect:"+targetUrl ;
    }


    /**
     * 删除板块
     * @param boardIds
     * @return
     */
    @RequestMapping(value = "/board/removeBoard",method = RequestMethod.GET)
    public String removeBoard(@RequestParam("boardIds")String boardIds){
        String[] arrIds = boardIds.split(",");
        for(String arrId : arrIds){
            forumService.removeBoard(new Integer(arrId));
        }
        String targetUrl = "/index.html";
        return "redirect:"+targetUrl ;
    }

    /**
     * 删除主题
     */
    @RequestMapping(value = "/board/removeTopic", method = RequestMethod.GET)
    public String removeTopic(@RequestParam("topicIds") String topicIds,@RequestParam("boardId") String boardId) {
        String[] arrIds = topicIds.split(",");
        for (int i = 0; i < arrIds.length; i++) {
            forumService.removeTopic(new Integer(arrIds[i]));
        }
        String targetUrl = "/board/listBoardTopics-" + boardId + ".html";
        return "redirect:"+targetUrl;
    }


    /**
     * 设置精华帖
     * @param topicIds
     * @param boardId
     * @return
     */
    @RequestMapping(value = "/board/makeDigestTopic",method = RequestMethod.GET)
    public String makeDigestTopic(@RequestParam("topicIds")String topicIds,@RequestParam("boardId")String boardId){
        String[] arrIds = topicIds.split(",");
        for(String arrId:arrIds){
            forumService.makeDigestTopic(new Integer(arrId));
        }
        String targetUrl = "/board/listBoardTopics-"+boardId+".html" ;
        return "redirect:"+targetUrl ;
    }


}
