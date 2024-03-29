package com.nju.web.servlet;

import com.alibaba.fastjson.JSON;
import com.nju.pojo.*;
import com.nju.service.LikelistService;
import com.nju.util.JsonString;
import com.nju.util.OperateUploadFileDTO;
import com.nju.util.OperateUploadFileUtil;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/likelist/*")
public class LikelistServlet extends BaseServlet{
    private LikelistService likelistService = new LikelistService();

    /*

    用户创建歌单

     */

    //GET 请求
    // 返回歌单信息
    public void likelistInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listname = request.getParameter("listname");
        //解决中文乱码
        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        //从session中获得用户名
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();

        //查找
        Likelist likelist = likelistService.searchLikelist(listname,username);

        //构建返回结构
        ResponseBody<Likelist> responseBody = new ResponseBody<Likelist>(0,"返回用户歌单成功",likelist);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //GET 返回某个歌单中的所有歌曲
    public void likelistContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int a = 1;
        String listname = request.getParameter("listname");
        //解决中文乱码
        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        //从session中获得用户名
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        System.out.println(listname);
        System.out.println(username);

        List<Music> musicList = likelistService.searchListMusic(listname,username);

        ResponseBody<List<Music>> responseBody = new ResponseBody<List<Music>>(musicList.size(),"返回歌单音乐成功",musicList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //POST 请求 收藏歌曲
    public void likeSong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        //从session中获得用户名
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();

        response.setContentType("text/json;charset=utf-8");

        if(user == null){
            response.getWriter().write(JSON.toJSONString(new ResponseBody<Boolean>(-1,"收藏失败",false)));
            return;
        }

        BufferedReader br = request.getReader();
        String str = br.readLine();

        JsonString jsonString =new JsonString(str);
        List<String> stringList = jsonString.parse();

        String songname = stringList.get(0);
        String singername = stringList.get(1);

        for(int i = 2;i<stringList.size();i++){
            String listname = stringList.get(i);
            System.out.println(listname);
            likelistService.likeSong(username,songname,singername,listname);
        }
        response.getWriter().write(JSON.toJSONString(new ResponseBody<Boolean>(0,"收藏成功",true)));
        return;
    }

    //POST 创建歌单
    public void creatLikelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        //从session中获得用户名
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();

        if (ServletFileUpload.isMultipartContent(request)){
            String contextPath = request.getContextPath();
            System.out.println("path: "+contextPath);

            OperateUploadFileDTO dto = OperateUploadFileUtil.parseParam(request);

            String listname = dto.getParamMap().get("listname");
            String description = dto.getParamMap().get("description");
            FileItem item = dto.getFileMap().get("file");

            try {
                item.write(new File("D:/JavaCode/MusicPlayer/src/main/webapp/src/images/likeListImage/"+item.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            likelistService.creatLikelist(username,listname,description,"src/images/likeListImage/"+item.getName());
        }
        else{
            System.out.println("创建歌单出错");
        }

        return;
    }

    /*

    系统推荐歌单

     */
    //GET 获取推荐歌单
    public void recommend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<SysLikelist> sysLikelist = likelistService.recommendSysLikelist();

        ResponseBody<List<SysLikelist>> responseBody = new ResponseBody<List<SysLikelist>>(8,"推荐歌单",sysLikelist);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    //GET 返回系统歌单信息
    public void syslikelistInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listname = request.getParameter("listname");
        //解决中文乱码
        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        //查找
        SysLikelist syslikelist = likelistService.searchSysLikelist(listname);

        //构建返回结构
        ResponseBody<SysLikelist> responseBody = new ResponseBody<SysLikelist>(0,"返回系统歌单成功",syslikelist);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);

        return;
    }

    //GET 返回某个歌单中的所有歌曲
    public void syslikelistContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listname = request.getParameter("listname");
        //解决中文乱码
        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        System.out.println(listname);

        List<Music> musicList = likelistService.searchListMusic(listname,"SYSTEM");

        ResponseBody<List<Music>> responseBody = new ResponseBody<List<Music>>(musicList.size(),"返回歌单音乐成功",musicList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

}
