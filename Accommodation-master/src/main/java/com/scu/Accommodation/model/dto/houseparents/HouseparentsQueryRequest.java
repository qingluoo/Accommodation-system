package com.scu.Accommodation.model.dto.houseparents;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询宿管请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HouseparentsQueryRequest extends PageRequest implements Serializable {

   /**
    * 主键
    */
    private Long id;

    /**
     * 
     */
    private String unionId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String park;

    /**
     * 
     */
    private String building;

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