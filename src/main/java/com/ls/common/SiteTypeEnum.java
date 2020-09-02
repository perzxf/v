package com.ls.common;

public enum SiteTypeEnum {
    /**
     * ['新闻网站', '微博', '贴吧', '论坛', '今日头条','知乎','微信公众号','其他']
     */

    NEWS("新闻网站",1),
    WEI_BO("微博",2),
    TIE_BA("贴吧",3),
    LUN_TAN("论坛",4),
    TOU_TIAO("今日头条",5),
    ZHI_HU("知乎",6),
    WEI_XIN("微信公众号",7),
    OTHER("其他",8),

    ;

    private SiteTypeEnum(String type, int expire) {
        this.type = type;
        this.expire = expire;
    }


    private String type;
    private int expire;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getExpire() {
        return Long.valueOf(expire);
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }


    public static void main(String[] args) {
        Long a = 6l;
        SiteTypeEnum[] values = SiteTypeEnum.values();
        for (SiteTypeEnum v:values){
            if (a == v.getExpire()) {
                System.out.println(v.getType());
            }
        }
    }

    public static String  getSiteExpire(Long expire){
        String typeText = null;
        SiteTypeEnum[] values = SiteTypeEnum.values();
        for (SiteTypeEnum v:values){
            if (expire == v.getExpire()) {
                typeText = v.getType();
            }
        }
        return  typeText;
    }

}
