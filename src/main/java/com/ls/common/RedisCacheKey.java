package com.ls.common;

public enum RedisCacheKey {

    OPENID("openid","微信用户唯一openid","String",-1),
    MONITOR("monitor","项目信息","String",-1),
            ;





    private RedisCacheKey(String type, String desc, String dataType, int expire) {
        this.type = type;
        this.desc = desc;
        this.dataType = dataType;
        this.expire = expire;
    }

    private String type;
    private String desc;
    private String dataType;
    private int expire;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public int getExpire() {
        return expire;
    }
    public void setExpire(int expire) {
        this.expire = expire;
    }

}
