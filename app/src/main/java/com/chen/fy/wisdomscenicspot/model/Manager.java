package com.chen.fy.wisdomscenicspot.model;

import org.litepal.crud.LitePalSupport;

public class Manager extends LitePalSupport {

    /**
     * 唯一标识
     */
    private int id;
    /**
     * 管理员账号
     */
    private String userId;
    /**
     * Hash加密
     */
    private String pwHash;
    private String pwSalt;

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
}