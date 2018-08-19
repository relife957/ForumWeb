package com.wangyi.web;


import com.wangyi.domain.Board;
import com.wangyi.domain.User;
import com.wangyi.service.ForumService;
import com.wangyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumManageController extends BaseController {
    private ForumService forumService ;

    private UserService userService ;

    @Autowired
    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 列出所有的论坛模块
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView listAllBoards(){
        ModelAndView view = new ModelAndView();
        List<Board> boards = forumService.getAllBoards() ;
        view.addObject("boards",boards);
        view.setViewName("/listAllBoards");
        return view ;
    }


    /**
     * 添加一个主题贴
     * @return
     */
    @RequestMapping(value = "/forum/addBoardPage",method = RequestMethod.GET)
    public String addBoardPage(){
        return "/addBoard" ;
    }
    @RequestMapping(value = "/forum/addBoard",method = RequestMethod.POST)
    public String addBoard(Board board){
        forumService.addBoard(board);
        return "/addBoardSuccess" ;
    }

    /**
     * 制定管理员的页面
     * @return
     */
    @RequestMapping(value = "/forum/setBoardManagerPage",method = RequestMethod.GET)
    public ModelAndView setBoardManagerPage(){
        ModelAndView view = new ModelAndView() ;
        List<Board> boards = forumService.getAllBoards();
        List<User> users = userService.getAllUsers();
        view.addObject("boards",boards) ;
        view.addObject("users",users);
        view.setViewName("/setBoardManager");
        return view ;
    }


    @RequestMapping(value = "/forum/setBoardManager",method = RequestMethod.POST)
    public ModelAndView setBoardManager(@RequestParam("userName")String userName,@RequestParam("boardId")String boardId){
        ModelAndView view  = new ModelAndView() ;
        User user = userService.getUserByUserName(userName);
        if(user == null){
            view.addObject("errorMsg","用户名("+userName+")不存在");
            view.setViewName("/fail");
        }else{
            Board board = forumService.getBoardById(Integer.parseInt(boardId)) ;
            user.getManBoards().add(board) ;
            userService.update(user);
            view.setViewName("/success");
        }
        return view ;
    }

    @RequestMapping(value="/forum/userLockManagePage",method = RequestMethod.GET)
    public ModelAndView userLockManage(){
        ModelAndView view = new ModelAndView() ;
        List<User> users = userService.getAllUsers();
        view.setViewName("/userLockManage");
        view.addObject("users",users);
        return view ;
    }

    @RequestMapping(value = "/forum/userLockManage",method = RequestMethod.POST)
    public ModelAndView userLockManage(@RequestParam("userName")String userName,
                                       @RequestParam("locked")String locked){
        ModelAndView view = new ModelAndView() ;
        User user = userService.getUserByUserName(userName);
        if(user == null){
            view.addObject("errorMsg","用户名不存在");
            view.setViewName("/fail");
        }else {
            user.setLocked(Integer.parseInt(locked));
            userService.update(user);
            view.setViewName("/success");
        }
        return view ;
    }



}
