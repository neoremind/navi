package com.baidu.beidou.navi.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Book implements Serializable {

    /**
     * 书籍ID
     */
    private int id;

    /**
     * 名称
     */
    private String name;

    public Book() {

    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.getId())
                .append("name", name).toString();
    }

}
