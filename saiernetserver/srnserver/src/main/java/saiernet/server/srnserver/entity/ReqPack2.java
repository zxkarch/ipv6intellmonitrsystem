package saiernet.server.srnserver.entity;

import com.alibaba.fastjson.JSONArray;

public class ReqPack2 {
    private String version;
    private JSONArray data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}