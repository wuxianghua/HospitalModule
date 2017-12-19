package com.palmap.huayitonglib.bean;

/**
 * Created by hengwei.guo on 2017/10/13 14:51.
 */

public class OfficeItemBean {

    private String title;
    private String imgUrl;

    public OfficeItemBean(String title, String imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
