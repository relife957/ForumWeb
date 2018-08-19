package com.wangyi.service;

import com.wangyi.Dao.LoginLogDao;
import com.wangyi.Dao.UserDao;
import com.wangyi.domain.LoginLog;
import com.wangyi.domain.User;
import com.wangyi.exception.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private UserDao userDao ;
    private LoginLogDao loginLogDao ;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setLoginLogDao(LoginLogDao loginLogDao) {
        this.loginLogDao = loginLogDao;
    }

    /**
     * 注册一个新用户,如果用户已经存在,抛出异常
     * @param user
     * @throws UserExistException
     */
    public void register(User user) throws UserExistException{
        User u = this.getUserByUserName(user.getUserName());
        if(u != null){
            throw new UserExistException("用户名已经存在!");
        }else{
            user.setCredit(100);
            user.setUserType(1);
            userDao.save(user);
        }
    }

    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    /**
     * 更新用户
     * @param user
     */
    public void update(User user){
        userDao.update(user);
    }

    public User getUserById(int userId){
        return userDao.get(userId);
    }

    /**
     * 将用户锁定
     * @param userName
     */
    public void lockUser(String userName){
        User user = userDao.getUserByUserName(userName);
        user.setLocked(User.USER_LOCK);
        userDao.update(user);
    }

    public void unlockUser(String userName){
        User user = userDao.getUserByUserName(userName);
        user.setLocked(User.USER_UNLOCK);
        userDao.update(user);
    }

    public List<User> queryUserByUserName(String userName){
        return userDao.queryUserByUserName(userName);
    }

    public List<User> getAllUsers(){
        return userDao.loadAll();
    }


    /**
     * 登陆成功
     * @param user
     */
    public void loginSuccess(User user){
        user.setCredit(5+user.getCredit());
        LoginLog loginLog = new LoginLog() ;
        loginLog.setUser(user);
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(new Date());
        userDao.update(user);
        loginLogDao.save(loginLog);
    }


}
