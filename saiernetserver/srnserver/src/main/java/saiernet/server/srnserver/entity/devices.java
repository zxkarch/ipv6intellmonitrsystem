package saiernet.server.srnserver.entity;

public class devices {
//     `id` int NOT NULL AUTO_INCREMENT,
//   `devicename` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
//   `typenote` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
//   `showtype` int NOT NULL,
//   `unit` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
    private int id;
    private String devicename;
    private String typenote;
    private int showtype;
    private String unit;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}