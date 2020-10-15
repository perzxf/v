package com.ls.entity.system;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

}
