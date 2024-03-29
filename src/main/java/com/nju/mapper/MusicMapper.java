package com.nju.mapper;

import com.nju.pojo.Music;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MusicMapper {
    //通过名字查找 名字可能是：歌名 歌手名 专辑名
    List<Music> selectByName(@Param("songname") String songname);

    //通过主键查找
    Music selectMusic(@Param("songname") String songname,@Param("singer") String singer);

    //通过歌手查询
    List<Music> selectBySinger(@Param("singername") String singername);

    //

    //增加音乐播放数
    void updatePlayCount(@Param("songname") String songname,@Param("singer") String singer);

    //增加音乐收藏量
    void updateLikeCount(@Param("songname") String songname,@Param("singer") String singer);

    //查询热门
    List<Music> selectTop(@Param("size")int size);

    //分页查询
    List<Music> selectByPage(@Param("begin") int begin, @Param("size") int size);
}
