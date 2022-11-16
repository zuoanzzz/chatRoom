package com.learn.qqcommon;

import java.io.Serializable;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String UID;
    private String pwd;

    public User() {}

    public User(String UID, String pwd) {
        this.UID = UID;
        this.pwd = pwd;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
