package com.nju.util;

import org.apache.tomcat.util.http.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;

public class OperateUploadFileDTO {
    private Map<String,String> paramMap;
    private Map<String,FileItem> fileMap;

    public OperateUploadFileDTO() {
        paramMap = new HashMap<>();
        fileMap = new HashMap<>();
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, FileItem> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, FileItem> fileMap) {
        this.fileMap = fileMap;
    }
}
