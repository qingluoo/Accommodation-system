package com.scu.Accommodation.model.dto.permission;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑权限请求
 *
 */
@Data
public class PermissionEditRequest implements Serializable {

    /**
     *
     */
    private Long id;

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
     *
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}