package com.crazy.portal.bean;

import java.util.List;

/**
 * Created by Bill on 2017/9/29.
 */
public class EmailParamsBean {
    private String title;
    private String content;
    private String emailTos;
    private String fileName;
    private List<String> datas;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getEmailTos() {
        return emailTos;
    }
    public void setEmailTos(String emailTos) {
        this.emailTos = emailTos;
    }
}
