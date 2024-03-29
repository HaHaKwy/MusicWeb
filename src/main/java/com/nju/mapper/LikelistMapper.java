package com.nju.mapper;

import com.nju.pojo.Likelist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikelistMapper {

    //通过用户名和歌单名唯一查找 用于歌单详情
    Likelist selectLikelist(@Param("listname") String listname,@Param("username") String username);

    //通过用户名查找所有歌单 用于收藏时
    List<String> selectByUser(@Param("username") String username);

    //更新歌单歌数 用于收藏时
    void updateSongNum(@Param("listname") String listname,@Param("username") String username);

    //创建歌单
    void insertLikelist(@Param("likelist") Likelist likelist);
}
