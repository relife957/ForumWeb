package com.wangyi.Dao;

import com.wangyi.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext({"wangyi-dao.xml" })
public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("wangyi-dao.xml");
        UserDao userDao = (UserDao)applicationContext.getBean("userDao");
        User user = userDao.getUserByUserName("tom") ;
        System.out.println(user.getUserName());
    }
}
