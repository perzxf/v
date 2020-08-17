package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;

import java.util.ArrayList;
import java.util.List;

public class SysMenu {
    /**
     * 菜单ID（主键）
     */
    @TableId
    private Long    menuId;

    /**
     * 菜单名称
     */
    private String  menuName;

    /**
     * 父菜单
     */
    private Long    parentId;

    /**
     * 请求地址
     */
    private String    url;

    /**
     * 菜单备注
     */
    private String    remark;

    /**
     * 子菜单
     */
    private transient List<SysMenu> children = new ArrayList<>();

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", parentId=" + parentId +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", children=" + children +
                '}';
    }
}
