package com.nju.web.servlet;
import com.alibaba.fastjson.JSON;
import com.nju.pojo.ResponseBody;
import com.nju.pojo.Singer;
import com.nju.service.SingerService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/singer/*")
public class SingerServlet extends BaseServlet{
    private SingerService singerService = new SingerService();
    //GET
    public void searchSinger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String singername = request.getParameter("singer");
        //解决中文乱码
        singername = new String(singername.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        //System.out.println(singername);

        //查找
        List<Singer> singerList = singerService.search(singername);
        //System.out.println(singerList);

        //构建返回结构
        ResponseBody<List<Singer>> responseBody = new ResponseBody<List<Singer>>(singerList.size(),"搜索歌手成功",singerList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //GET
    public void allSinger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //查找
        List<Singer> singerList = singerService.searchAll();
        System.out.println(singerList);

        //构建返回结构
        ResponseBody<List<Singer>> responseBody = new ResponseBody<List<Singer>>(singerList.size(),"搜索歌手成功",singerList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    public void searchOneSinger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String singername = request.getParameter("singername");
        //解决中文乱码
        singername = new String(singername.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        System.out.println(singername);

        //查找
        Singer singer = singerService.searchOne(singername);
//        System.out.println(singer);

        //构建返回结构
        ResponseBody<Singer> responseBody = new ResponseBody<Singer>(0,"歌手详情",singer);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

}
