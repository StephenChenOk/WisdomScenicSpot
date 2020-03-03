package com.chen.fy.wisdomscenicspot.model;

import org.litepal.crud.LitePalSupport;

public class Visitor extends LitePalSupport{

    /**
     * 用户唯一标识
     */
    private int id;
    /**
     * 登陆账号
     */
    private String userId;
    /**
     * 昵称
     */
    private String userName;
    /**
     * Hash加密
     */
    private String pwHash;
    private String pwSalt;
    /**
     * 手机号码
     */
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public String getPwSalt() {
        return pwSalt;
    }

    public void setPwSalt(String pwSalt) {
        this.pwSalt = pwSalt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}