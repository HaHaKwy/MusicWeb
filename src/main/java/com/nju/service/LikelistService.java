package com.nju.service;

import com.nju.mapper.CollectionMapper;
import com.nju.mapper.LikelistMapper;
import com.nju.mapper.MusicMapper;
import com.nju.mapper.SysLikelistMapper;
import com.nju.pojo.CollectionRelation;
import com.nju.pojo.Likelist;
import com.nju.pojo.Music;
import com.nju.pojo.SysLikelist;
import com.nju.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LikelistService {
    //创建SqlSessionFactory 工厂对象
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    /*

    用户创建歌单服务

     */

    public Likelist searchLikelist(String listname,String username) {
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        LikelistMapper mapper = sqlSession.getMapper(LikelistMapper.class);
        //2.4 调用方法
        Likelist likelist =  mapper.selectLikelist(listname,username);
        //2.5 释放资源
        sqlSession.close();
        return likelist;
    }

    public List<Music> searchListMusic(String listname,String username){
        //2.2 获取SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //2.3 获取Mapper
        CollectionMapper mapper = sqlSession.getMapper(CollectionMapper.class);
        //2.4 调用方法
        List<Music> musicList =  mapper.seletMusicByList(listname,username);
        //2.5 释放资源
        sqlSession.close();
        return musicList;
    }

    public void likeSong(String username,String songname,String singername,String listname){
        SqlSession sqlSession = factory.openSession();

        //更新收藏关系表
        CollectionMapper mapper1 = sqlSession.getMapper(CollectionMapper.class);
        CollectionRelation coll = new CollectionRelation(username,songname,singername,listname);
        CollectionRelation c2 = mapper1.selectColl(coll);
        if(c2 != null){
            return;
        }
        mapper1.addLike(coll);
        sqlSession.commit();

        //更新歌单表
        LikelistMapper mapper2 = sqlSession.getMapper(LikelistMapper.class);
        mapper2.updateSongNum(listname,username);
        sqlSession.commit();

        //更新歌曲收藏量
        MusicMapper mapper3 = sqlSession.getMapper(MusicMapper.class);
        mapper3.updateLikeCount(songname,singername);
        sqlSession.commit();

        sqlSession.close();
        return;
    }

    public void creatLikelist(String username,String listname,String description,String imagePath){
        SqlSession sqlSession = factory.openSession();

        //更新歌单表
        LikelistMapper mapper = sqlSession.getMapper(LikelistMapper.class);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(date);

        Likelist likelist = new Likelist(listname,username,0,dateStr,description,imagePath);

        mapper.insertLikelist(likelist);
        sqlSession.commit();

        sqlSession.close();
        return;
    }


    /*

    系统歌单服务

     */
    public SysLikelist searchSysLikelist(String listname) {
        SqlSession sqlSession = factory.openSession();
        SysLikelistMapper mapper = sqlSession.getMapper(SysLikelistMapper.class);

        //返回歌单详情
        SysLikelist sysLikelist =  mapper.selectSysLikelist(listname);
        //增加点击量
        mapper.updateClickCount(listname);

        sqlSession.commit();
        sqlSession.close();

        return sysLikelist;
    }

    public List<SysLikelist> recommendSysLikelist() {
        SqlSession sqlSession = factory.openSession();
        SysLikelistMapper mapper = sqlSession.getMapper(SysLikelistMapper.class);

        //随机起点
        int begin = ((int)(Math.random()*100)) % 15; // 15是总歌单数
        List<SysLikelist> sysLikelist = new ArrayList<>();

        if(begin <= 7){
            sysLikelist =  mapper.selectByPage(begin,8);
        }
        else{
            sysLikelist = mapper.selectByPage(begin,15 - begin);
            sysLikelist.addAll(mapper.selectByPage(0,begin-7));
        }
        sqlSession.close();
        return sysLikelist;
    }

}