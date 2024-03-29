package com.nju.web.servlet;

import com.alibaba.fastjson.JSON;
import com.nju.pojo.Comment;
import com.nju.pojo.ResponseBody;
import com.nju.pojo.User;
import com.nju.service.CommentService;
import com.nju.util.JsonString;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/comment/*")
public class CommentServlet extends BaseServlet{
    private CommentService commentService = new CommentService();

    //GET 获取某歌单全部评论
    public void allComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listname = request.getParameter("listname");
        String creator = request.getParameter("creator");
        //解决中文乱码
        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        creator = new String(creator.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        //查找
        List<Comment> commentList = commentService.searchAllComment(listname,creator);

        //构建返回结构
        ResponseBody<List<Comment>> responseBody = new ResponseBody<List<Comment>>(commentList.size(),"返回歌单评论成功",commentList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //POST 评论某个歌单
    public void publishComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        //从session中获得用户名
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();

        BufferedReader br = request.getReader();
        String str = br.readLine();

        JsonString jsonString =new JsonString(str);
        List<String> stringList = jsonString.parse();

        String listname = stringList.get(0);
        String creator = stringList.get(1);
        String text = stringList.get(2);
        String timestamp = stringList.get(3);

        commentService.creatComment(username,listname,creator,text,timestamp);

        return;
    }

}
