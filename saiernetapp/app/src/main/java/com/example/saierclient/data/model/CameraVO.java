package com.example.saierclient.data.model;

import java.sql.Date;

public class CameraVO {
    private int id;
    private String devicename;
    private String typenote;
    private int showtype;
    private Date rltime;
    private String rvalue;
    private String imgurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getTypenote() {
        return typenote;
    }

    public void setTypenote(String typenote) {
        this.typenote = typenote;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public Date getRltime() {
        return rltime;
    }

    public void setRltime(Date rltime) {
        this.rltime = rltime;
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
