package com.nju.web.servlet;


import com.alibaba.fastjson.JSON;
import com.nju.pojo.Lyric;
import com.nju.pojo.Music;
import com.nju.pojo.ResponseBody;
import com.nju.service.MusicService;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/music/*")
public class MusicServlet  extends BaseServlet{
    private MusicService musicService = new MusicService();

    //GET 请求
    //综合 模糊搜索 歌曲
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String songname = request.getParameter("songname");
        //解决中文乱码
        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        //查找
        List<Music> musicList = musicService.search(songname);

        //构建返回结构
        ResponseBody<List<Music>> responseBody = new ResponseBody<List<Music>>(musicList.size(),"搜索音乐成功",musicList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //System.out.println(jsonString);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //播放音乐 返回音乐文件数据
    public void play(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String songname = request.getParameter("songname");
        String singer = request.getParameter("singer");

        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        singer = new String(singer.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        System.out.println(songname);
        System.out.println(singer);

        //增加播放数
        musicService.increasePlayCount(songname,singer);

        //返回歌曲信息
        Music music = musicService.getMusicInfo(songname,singer);
        String jsonString = JSON.toJSONString(new ResponseBody<Music>(0,"返回歌曲路径成功",music));
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);

        return;
    }

    //获取歌词文件
    public void lyric(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String songname = request.getParameter("songname");
        String singer = request.getParameter("singer");

        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        singer = new String(singer.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        List<Lyric> lyrics = musicService.getLyrics(songname,singer);

        String jsonString;
        if (lyrics != null) {
            jsonString = JSON.toJSONString(new ResponseBody<List<Lyric>>(lyrics.size(),"返回歌词文件",lyrics));
        }
        else{
            jsonString = JSON.toJSONString(new ResponseBody<String>(-1,"未找到歌词文件",""));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //获取歌词文件
    public void beats(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String songname = request.getParameter("songname");
        String singer = request.getParameter("singer");

        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        singer = new String(singer.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        List<Lyric> lyrics = musicService.getBeats(songname,singer);

        String jsonString;
        if (lyrics != null) {
            jsonString = JSON.toJSONString(new ResponseBody<List<Lyric>>(lyrics.size(),"返回歌词文件",lyrics));
        }
        else{
            jsonString = JSON.toJSONString(new ResponseBody<String>(-1,"未找到歌词文件",""));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //切歌
    public void switchSong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String listname = request.getParameter("listname");
        String creator = request.getParameter("creator");
        String songname = request.getParameter("songname");
        String singer = request.getParameter("singer");
        String flagStr = request.getParameter("flag");

        listname = new String(listname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        creator = new String(creator.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        singer = new String(singer.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        Music nextMusic = musicService.switchMusic(listname,creator,songname,singer,Integer.parseInt(flagStr));
        String jsonString;
        if(nextMusic!=null){
            jsonString = JSON.toJSONString(new ResponseBody<Music>(0,"返回歌曲",nextMusic));
        }
        else{
            jsonString = JSON.toJSONString(new ResponseBody<String>(-1,"无更多歌曲",null));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //返回一个歌手演唱的歌曲
    public void searchBySinger(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String singername = request.getParameter("singername");
        //解决中文乱码
        singername = new String(singername.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        //查找
        List<Music> musicList = musicService.searchBySinger(singername);

        //构建返回结构
        ResponseBody<List<Music>> responseBody = new ResponseBody<List<Music>>(musicList.size(),"返回歌手演唱歌曲成功",musicList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //返回推荐的歌曲
    public void topSong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Music> musicList = musicService.searchTop(10);

        //构建返回结构
        ResponseBody<List<Music>> responseBody = new ResponseBody<List<Music>>(musicList.size(),"返回热门歌曲成功",musicList);
        //转成JSON字符串
        String jsonString = JSON.toJSONString(responseBody);
        //写回
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
        return;
    }

    //下载歌曲
    public void downloadSong(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String songname = request.getParameter("songname");
        String singer = request.getParameter("singer");
        songname = new String(songname.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        singer = new String(singer.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);

        String filename = singer+" - "+songname+".mp3";

        // 2. 获取ServletContext对象，用于获取文件对应的MIME类型
        ServletContext servletContext = getServletContext();

        // 3. 获取文件名对应的MIME类型
        String mimeType = servletContext.getMimeType("src/Songs/" + filename);
        System.out.println(mimeType);

        // 4. 将对应的MIME类型设置为响应时的数据类型
        response.setContentType(mimeType);

        // 5. 设置响应头，告知浏览器要对文件做下载的操作
        String downloadName = new String(filename.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition", "attachment;filename="+downloadName);

        // 6. 获取文件的路径，并转成输入流
        InputStream inOfFile = servletContext.getResourceAsStream("src/Songs/" + filename);

        // 7. 获取响应的输出流
        ServletOutputStream outOfFile = response.getOutputStream();

        // 8. 通过IO工具类，直接将输入流复制到输出流，然后写到浏览器客服端
        IOUtils.copy(inOfFile, outOfFile);
        return;
    }
}
