package com.baidu.beidou.navimgr.zoo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * ClassName: ZooNode <br/>
 * Function: zookeeper节点
 * 
 * @author Zhang Xu
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ZooNode {

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点path
     */
    @JsonIgnore
    private String path;

    /**
     * 是否是叶子节点
     */
    private Boolean leaf;

    /**
     * 前端是否打开
     */
    private Boolean open;

    /**
     * 子节点列表
     */
    private List<ZooNode> children = new ArrayList<ZooNode>(8);

    /**
     * Creates a new instance of ZooNode.
     * 
     * @param path
     *            全路径
     * @param name
     *            名称
     */
    public ZooNode(String path, String name) {
        super();
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "[name:" + name + ", children:" + children + (leaf == true ? ", leaf:true" : "")
                + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ZooNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZooNode> children) {
        this.children = children;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

}
