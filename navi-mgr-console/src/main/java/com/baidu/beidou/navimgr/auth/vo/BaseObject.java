package com.baidu.beidou.navimgr.auth.vo;

import java.io.Serializable;

/**
 * @author zhangxu
 */
public abstract class BaseObject<K extends Serializable> implements Serializable, Cloneable {

    private static final long serialVersionUID = 8854260536880579592L;

    /**
     * 该对象的业务主键
     */
    protected K id;

    /**
     * @return the id
     */
    public K getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(K id) {
        this.id = id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        K k1 = this.getId();
        @SuppressWarnings("unchecked")
        K k2 = (K) ((BaseObject) obj).getId();
        return k1 != null && k2 != null && k1.equals(k2);
    }
}

