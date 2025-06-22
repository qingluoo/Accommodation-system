package com.scu.Accommodation.model.dto.housemanager;

import lombok.Data;

import java.io.Serializable;


/**
 * 创建宿舍总管请求
 *
 */
@Data
public class HouseManagerAddRequest implements Serializable {

   private Long id;

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
    private String room;

    /**
     * 
     */
    private Integer bedNum;

    /**
     * 
     */
    private Integer liveNum;

    /**
     * 
     */
    private String roomType;

    /**
     * 
     */
    private Integer isFull;


    /**
     * 
     */
    private String phone;

    /**
     *
     */
    private Long userId;


    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}