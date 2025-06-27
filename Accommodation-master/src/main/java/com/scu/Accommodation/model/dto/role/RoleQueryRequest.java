package com.scu.Accommodation.model.dto.role;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询角色请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQueryRequest extends PageRequest implements Serializable {

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

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}