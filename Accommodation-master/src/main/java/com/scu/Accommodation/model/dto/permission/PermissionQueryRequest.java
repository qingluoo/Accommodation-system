package com.scu.Accommodation.model.dto.permission;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询权限请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 
     */
    private Integer roleId;

    /**
     * 
     */
    private String roleName;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Integer isDelete;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}