package com.example.saierclient.data.model;

public class ReqVO {
    public ReqVO(int showtype){this.showtype = showtype;}
    private int showtype;

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }
}
