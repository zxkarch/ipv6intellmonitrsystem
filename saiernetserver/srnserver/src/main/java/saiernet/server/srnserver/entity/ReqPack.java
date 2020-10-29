package saiernet.server.srnserver.entity;

import com.alibaba.fastjson.JSONObject;

public class ReqPack {
    private String version;
    private JSONObject data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

}