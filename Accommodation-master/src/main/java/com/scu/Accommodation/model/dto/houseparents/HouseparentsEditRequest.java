package com.scu.Accommodation.model.dto.houseparents;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑宿管请求
 *
 */
@Data
public class HouseparentsEditRequest implements Serializable {

    /**
     *
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

    private Long userId;

    private static final long serialVersionUID = 1L;
}