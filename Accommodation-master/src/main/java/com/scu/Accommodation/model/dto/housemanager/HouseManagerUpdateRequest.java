package com.scu.Accommodation.model.dto.housemanager;

import lombok.Data;

import java.io.Serializable;


/**
 * 更新宿舍总管请求
 *
 */
@Data
public class HouseManagerUpdateRequest implements Serializable {

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
    private Integer isDelete;


    private Long userId;

    private static final long serialVersionUID = 1L;
}