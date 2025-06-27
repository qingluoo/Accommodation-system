package com.scu.Accommodation.model.dto.role;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑角色请求
 *
 */
@Data
public class RoleEditRequest implements Serializable {

    /**
     * 标题
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;


    /**
     * 权限列表
     */
    private List<String> codelist;

    private static final long serialVersionUID = 1L;
}