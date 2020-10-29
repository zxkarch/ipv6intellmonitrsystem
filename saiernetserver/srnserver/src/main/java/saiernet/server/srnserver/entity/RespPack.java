package saiernet.server.srnserver.entity;

public class RespPack<T> {
    private int code;
    private String desc;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //*******************************************************************
    public static RespPack respPackSuc(Object data) {

        RespPack respPack = new RespPack();
        respPack.setCode(0);
        respPack.setDesc("成功");
        respPack.setData(data);

        return respPack;
    }

    public static RespPack respPackSuc() {

        return respPackSuc(null);
    }

    public static RespPack respPackFail(int code, String desc, Object data) {

        RespPack respPack = new RespPack();
        respPack.setCode(code);
        respPack.setDesc(desc);
        respPack.setData(data);

        return respPack;
    }

    public static RespPack respPackFail(int code, String desc) {

        return respPackFail(code, desc, null);
    }
}