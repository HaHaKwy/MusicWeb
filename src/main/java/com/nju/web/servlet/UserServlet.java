package com.nju.web.servlet;

import com.alibaba.fastjson.JSON;
import com.nju.pojo.ResponseBody;
import com.nju.pojo.User;
import com.nju.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet  extends BaseServlet{
    private UserService userService = new UserService();

    //POST 请求
    public void Login_servlet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        BufferedReader br = request.getReader();
        String params = br.readLine();
        // 将JSON字符串转为Java对象
        User user1 = JSON.parseObject(params, User.class);
        String username = user1.getUsername();
        String password = user1.getPassword();
        System.out.println(user1.getUsername());
        System.out.println(user1.getPassword());

        //调用userService的login方法
        User user = userService.login(username, password);

        String jsonString;
        //登陆成功
        if(user != null && user.getPassword().equals(password)){
            //将登陆成功后的user对象，存储到session
            HttpSession session = request.getSession();
            session.setAttribute("user",user);

            jsonString = JSON.toJSONString(new ResponseBody<User>(0,"登陆成功！",user));
            System.out.println("登陆成功！");
        }
        //登陆失败
        else{
            if(user == null){
                jsonString = JSON.toJSONString(new ResponseBody<Boolean>(-1,"未找到用户",false));
                System.out.println("未找到用户");
            }
            else{
                jsonString = JSON.toJSONString(new ResponseBody<Boolean>(-1,"密码错误，你是认真的吗？",false));
                System.out.println("密码错误，你是认真的吗？");
            }
        }
        //先设置字符集 再获取writer对象
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    //POST 请求
    public void Signup_servlet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        BufferedReader br = request.getReader();
        String params = br.readLine();
        // 将JSON字符串转为Java对象
        User user1 = JSON.parseObject(params, User.class);
        String username = user1.getUsername();
        String password = user1.getPassword();
        String email = user1.getEmail();

        String jsonString;
        response.setContentType("text/json;charset=utf-8");
        PrintWriter writer = response.getWriter();

        if(!email.contains("@")){
            jsonString = JSON.toJSONString(new ResponseBody<Boolean>(-1,"邮箱格式不正确，你是认真的吗？",false));
            writer.write(jsonString);
            return;
        }

        boolean flag = userService.register(username, password, email);

        if(flag == false || username == null || "SYSTEM".equals(username)){
            jsonString = JSON.toJSONString(new ResponseBody<Boolean>(-1,"用户名重复，换一个试试~",false));
        }
        else{
            //创建用户默认歌单
            userService.creatDefaultList(username);

            jsonString = JSON.toJSONString(new ResponseBody<Boolean>(0,"注册成功！",true));
        }
        writer.write(jsonString);
    }

    //POST 退出登陆
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        return;
    }


    // GET请求 返回用户信息
    public void userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String jsonString;
        if(user == null){
            jsonString = JSON.toJSONString(new ResponseBody<User>(-1,"未登陆",null));
        }
        else{
            jsonString = JSON.toJSONString(new ResponseBody<User>(0,"个人信息",user));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    // GET请求 返回用户创建的歌单名列表
    public void allLikelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        //查找
        List<String> llList = userService.allLikelist(user.getUsername());
        System.out.println(llList);

        //构建返回结构
        ResponseBody<List<String>> responseBody = new ResponseBody<List<String>>(llList.size(),"我的全部歌单",llList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }
}
