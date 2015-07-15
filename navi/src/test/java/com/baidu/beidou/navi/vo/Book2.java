package com.baidu.beidou.navi.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Book2 {

    /**
     * 书籍ID
     */
    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 增加的新列
     */
    private String extraField;

    public Book2() {

    }

    public Book2(int id, String name, String extraField) {
        this.id = id;
        this.name = name;
        this.extraField = extraField;
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

    public String getExtraField() {
        return extraField;
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.getId())
                .append("name", name).append("extraField", extraField).toString();
    }

}
