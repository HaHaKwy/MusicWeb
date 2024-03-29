package com.nju.mapper;

import com.nju.pojo.CollectionRelation;
import com.nju.pojo.Music;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionMapper {

    //查询某个用户某个歌单中的歌曲
    List<Music> seletMusicByList(@Param("listname") String listname,@Param("username") String username);

    //查询是否存在
    CollectionRelation selectColl(@Param("coll") CollectionRelation coll);

    //收藏某个歌
    void addLike(@Param("coll") CollectionRelation coll);
}
