package com.nju.service;

import com.nju.mapper.CommentMapper;
import com.nju.pojo.Comment;
import com.nju.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class CommentService {
    //创建SqlSessionFactory 工厂对象
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //获取某歌单全部评论
    public List<Comment> searchAllComment(String listname, String creator) {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);
        //2.4 调用方法
        List<Comment> commentList =  mapper.selectComment(listname,creator);
        //2.5 释放资源
        sqlSession.close();
        return commentList;
    }

    public void creatComment(String username,String listname, String creator,String text,String timestamp) {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        CommentMapper mapper = sqlSession.getMapper(CommentMapper.class);
        //2.4 调用方法
        Comment comment = new Comment(username,listname,creator,text,timestamp);
        mapper.insertComment(comment);
        //2.5 释放资源
        sqlSession.commit();
        sqlSession.close();

        return;
    }
}