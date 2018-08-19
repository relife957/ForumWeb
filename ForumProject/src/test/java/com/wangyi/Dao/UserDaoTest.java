package com.wangyi.Dao;

import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG ;
import com.wangyi.domain.User;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import static org.junit.Assert.* ;


@SpringApplicationContext({"wangyi-dao.xml" })
public class UserDaoTest extends UnitilsTestNG{

    @SpringBean("userDao")
    private UserDao userDao ;

    @Test
    public void findUser(){
        User user = userDao.getUserByUserName("tom");
        assertNotNull("tom存在",user);
        assertEquals("tom",user.getUserName());
    }
}
