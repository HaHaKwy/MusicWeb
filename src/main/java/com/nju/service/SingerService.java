package com.nju.service;

import com.nju.mapper.SingerMapper;
import com.nju.pojo.Singer;
import com.nju.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
public class SingerService {
    //创建SqlSessionFactory 工厂对象
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    //通过歌手名模糊搜索
    public List<Singer> search(String singername) {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        SingerMapper mapper = sqlSession.getMapper(SingerMapper.class);
        //2.4 调用方法
        singername = "%"+singername+"%";
        List<Singer> singerList = (List<Singer>) mapper.selectSinger(singername);
        //2.5 释放资源
        sqlSession.close();
        return singerList;
    }

    public List<Singer> searchAll() {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        SingerMapper mapper = sqlSession.getMapper(SingerMapper.class);
        //2.4 调用方法
        List<Singer> singerList = (List<Singer>) mapper.selectAllSinger();
        //2.5 释放资源
        sqlSession.close();
        return singerList;
    }

    public Singer searchOne(String singername) {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        SingerMapper mapper = sqlSession.getMapper(SingerMapper.class);
        //2.4 调用方法
        singername = singername;
        Singer singer = (Singer) mapper.selectOneSinger(singername);
        //2.5 释放资源
        sqlSession.close();
        return singer;
    }

}