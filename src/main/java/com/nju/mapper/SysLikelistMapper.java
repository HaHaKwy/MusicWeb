package com.nju.mapper;

import com.nju.pojo.SysLikelist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLikelistMapper {

    SysLikelist selectSysLikelist(@Param("listname") String listname);
    List<SysLikelist> selectByPage(@Param("begin") int begin, @Param("size") int size);
    void updateClickCount(@Param("listname") String listname);
}
