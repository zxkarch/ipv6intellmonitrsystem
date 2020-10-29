package saiernet.server.srnserver.entity;

public class SensorVO {
    private String devicename;
    private String typenote;
    private String rtime;
    private String rvalue;
    private String imgurl;

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