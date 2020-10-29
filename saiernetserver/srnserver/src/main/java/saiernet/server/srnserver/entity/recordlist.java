package saiernet.server.srnserver.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

public class recordlist {
    // `id` int NOT NULL,
    // `iddev` int NOT NULL,
    // `rtime` datetime NOT NULL,
    // `rvalue` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
    // `imgurl` varchar(85) COLLATE utf8_unicode_ci DEFAULT NULL,
    private int id;
    private int iddev;
    // @DateTimeFormat(pattern="yyyy-MM-dd")//set
    // @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")//get
    private String rtime;
    private String rvalue;
    private String imgurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIddev() {
        return iddev;
    }

    public void setIddev(int iddev) {
        this.iddev = iddev;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getRvalue() {
        return rvalue;
    }

    public void setRvalue(String rvalue) {
        this.rvalue = rvalue;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}