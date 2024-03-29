package com.nju.mapper;

import com.nju.pojo.Singer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SingerMapper {

    List<Singer> selectSinger(@Param("singername") String singername);
    List<Singer> selectAllSinger();
    Singer selectOneSinger(@Param("singername") String singername);
}
