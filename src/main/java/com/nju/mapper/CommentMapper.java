package com.nju.mapper;

import com.nju.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {

    List<Comment> selectComment(@Param("listname") String listname,@Param("creator") String creator);
    void insertComment(@Param("comment") Comment comment);
}
