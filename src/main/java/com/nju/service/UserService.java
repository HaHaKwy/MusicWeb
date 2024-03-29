package com.nju.service;

import com.nju.mapper.LikelistMapper;
import com.nju.mapper.UserMapper;
import com.nju.pojo.Likelist;
import com.nju.pojo.User;
import com.nju.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserService {
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    public User login(String username, String password){
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //2.4 调用方法
        User user = mapper.selectByName(username);
        //2.5 释放资源
        sqlSession.close();

        return user;
    }

    public boolean register(String username,String password, String email){
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //2.4 调用方法
        User user = mapper.selectByName(username);

        boolean flag = false;
        if(user == null){
            mapper.addUser(username,password,email);
            sqlSession.commit();
            flag = true;
        }
        //2.5 释放资源
        sqlSession.close();
        return flag;
    }

    public List<String> allLikelist(String username){
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取收藏表的Mapper
        LikelistMapper mapper = sqlSession.getMapper(LikelistMapper.class);
        //2.4 调用方法
        List<String> llList = (List<String>) mapper.selectByUser(username);
        //2.5 释放资源
        sqlSession.close();
        return llList;
    }

    public void creatDefaultList(String username){
        SqlSession sqlSession = factory.openSession();
        LikelistMapper mapper = sqlSession.getMapper(LikelistMapper.class);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(date);

        Likelist likelist = new Likelist("我喜欢的音乐",username,0,dateStr,"你喜欢的，值得被聆听","src/images/sysLikeListImage/like.jpg");

        mapper.insertLikelist(likelist);

        sqlSession.commit();
        sqlSession.close();
        return;
    }
}
