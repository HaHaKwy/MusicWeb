package com.nju.service;

import com.nju.mapper.CollectionMapper;
import com.nju.mapper.MusicMapper;
import com.nju.pojo.Lyric;
import com.nju.pojo.Music;
import com.nju.util.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MusicService {
    //创建SqlSessionFactory 工厂对象
    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();

    //通过歌名模糊搜索
    public List<Music> search(String songname) {
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);
        songname = "%"+songname+"%";
        List<Music> music = (List<Music>) mapper.selectByName(songname);
        sqlSession.close();
        return music;
    }

    //通过歌名+歌手精确搜索 用于播放
    public byte[] getMusicData(String songname, String singer) throws IOException {
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);

        Music music = mapper.selectMusic(songname,singer);
        sqlSession.close();

        //String contextPath = req.getContextPath();
        String path = "D:/music/"+music.getPath()+songname+"-"+singer+".mp3";
        //D:/music/jay/七里香-周杰伦.mp3
        System.out.println(path);

        byte[] data = null;
        File file = new File(path);
        data = Files.readAllBytes(file.toPath());
        return data;
    }

    //返回歌曲路径
    public Music getMusicInfo(String songname, String singername) throws IOException {
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);
        Music music = mapper.selectMusic(songname,singername);
        sqlSession.close();
        return music;
    }

    //播放音乐为音乐增加播放数
    public void increasePlayCount(String songname,String singer){
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);
        mapper.updatePlayCount(songname,singer);
        sqlSession.commit();
        sqlSession.close();
    }

    public List<Music> searchBySinger(String singername) {
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);
        List<Music> music = (List<Music>) mapper.selectBySinger(singername);
        sqlSession.close();
        return music;
    }

    public List<Music> searchTop(int num) {
        SqlSession sqlSession = factory.openSession();
        MusicMapper mapper = sqlSession.getMapper(MusicMapper.class);
        List<Music> musicList = (List<Music>) mapper.selectTop(num);
        sqlSession.close();
        return musicList;
    }

    public List<Lyric> getLyrics(String songname,String singername){
        try {
            List<Lyric> lyrics = new ArrayList<>();
            String filePath = "D:\\JavaCode\\MusicPlayer\\src\\main\\webapp\\src\\Lyrics\\"+singername+" - "+songname+".lrc";

            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null) {
                    if(lineTxt.length() <= 1 || lineTxt.charAt(1)!='0'){
                        continue;
                    }
                    //[00:12.12]
                    float time = Integer.parseInt(lineTxt.substring(1,3)) *60 +Float.parseFloat(lineTxt.substring(4,9));
                    String lrc = lineTxt.substring(10);
                    Lyric lyric = new Lyric(time,lrc);
                    lyrics.add(lyric);
                }
                read.close();
                return lyrics;
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }

    public List<Lyric> getBeats(String songname,String singername){
        try {
            List<Lyric> lyrics = new ArrayList<>();
            String filePath = "D:\\JavaCode\\MusicPlayer\\src\\main\\webapp\\src\\Beats\\"+singername+" - "+songname+".lrc";

            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null) {
                    float time = Integer.parseInt(lineTxt.substring(1,3)) *60 +Float.parseFloat(lineTxt.substring(4,9));
                    String lrc = lineTxt.substring(10);
                    Lyric lyric = new Lyric(time,lrc);
                    lyrics.add(lyric);
                }
                read.close();
                return lyrics;
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }

    //切歌
    public Music switchMusic(String listname,String creator,String songname,String singer,Integer flag) {
        SqlSession sqlSession = factory.openSession();
        //获取当前歌单所有歌曲
        CollectionMapper mapper = sqlSession.getMapper(CollectionMapper.class);

        System.out.println("======== in switchMusic ========");
        System.out.println(listname);
        System.out.println(creator);
        System.out.println(songname);
        System.out.println(singer);
        System.out.println("===================");
        List<Music> musicList = (List<Music>) mapper.seletMusicByList(listname,creator);
        sqlSession.close();

        //只有一首歌
        int len = musicList.size(),i;
        if(len == 1){
            return null;
        }

        //定位当前歌曲
        for(i = 0;i<len;++i){
            if(musicList.get(i).getSongname().equals(songname) && musicList.get(i).getSinger().equals(singer)){
                break;
            }
        }
        //返回上一首或下一首
        if(flag == 0){
            return musicList.get((i+len-1)%len);
        }
        else if(flag == 1){
            return musicList.get((i+1)%len);
        }
        return null;
    }
}