package com.scu.Accommodation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName apartment
 */
@TableName(value ="apartment")
@Data
public class Apartment {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Integer isDelete;
}