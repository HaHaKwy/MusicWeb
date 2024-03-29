package com.nju.util;

import java.util.ArrayList;
import java.util.List;

//解析Json字符串 用于接收不完整的类的数据 不需要重新定义类
public class JsonString {
    private String jsonString;

    public JsonString(String jsonStr) {
        this.jsonString = jsonStr;
    }
    public List<String> parse(){
        List<String> stringList = new ArrayList<String>();

        jsonString = jsonString.substring(1, jsonString.length()-1);
        String[] strList = jsonString.split(",");

        for(String str:strList){
            str = str.substring(str.indexOf(':')+2,str.length()-1);
            stringList.add(str);
        }
        return stringList;
    }
}
