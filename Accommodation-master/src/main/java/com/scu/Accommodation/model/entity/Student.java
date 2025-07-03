package com.scu.Accommodation.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String unionId;

    /**
     * 
     */
    private Integer grade;

    /**
     * 
     */
    private String stuName;

    /**
     * 
     */
    private Integer sex;

    /**
     * 
     */
    private String college;

    /**
     * 
     */
    private String major;

    /**
     * 
     */
    private String classNum;

    private String park;

    private String building;

    private String room;

    /**
     * 
     */
    private String roomId;

    private Long apartment_id;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}