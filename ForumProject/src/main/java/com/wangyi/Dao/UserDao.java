package com.wangyi.Dao;

import com.wangyi.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao<User> {
    private static final String GET_USER_BY_USERNAME="from User u where u.userName = ?" ;

    private static final String QUERY_USER_BY_USERNAME="from User u where u.userName like ?";

    /**
     * 精确查询,根据username查找user对象
     * @param userName
     * @return
     */
    public User getUserByUserName(String userName){
        List<User> users = (List<User>)getHibernateTemplate().find(GET_USER_BY_USERNAME,userName);
        if(users.size() == 0){
            return null ;
        }
        return users.get(0);
    }


    /**
     * 模糊查询,根据传入的username作为前缀,查找以username为前缀的user对象
     * @param userName
     * @return
     */
    public List<User> queryUserByUserName(String userName){
        return (List<User>)getHibernateTemplate().find(QUERY_USER_BY_USERNAME,userName+"%");
    }
}
