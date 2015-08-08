package com.baidu.beidou.navimgr.auth.vo;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * ClassName: User <br/>
 * Function: 用户实体
 * 
 * @author Zhang Xu
 */
public class User extends BaseObject<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的userId
     */
    private int userId;

    /**
     * 密码MD5
     */
    private String passwordMd5;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色
     */
    private Set<String> roles = Sets.newHashSet();

    /**
     * 用户权限
     */
    private Set<String> auths = Sets.newHashSet();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getAuths() {
        return auths;
    }

    public void setAuths(Set<String> auths) {
        this.auths = auths;
    }

}
