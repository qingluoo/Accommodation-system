package com.scu.Accommodation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName teacher
 */
@TableName(value ="teacher")
@Data
public class Teacher {
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
    private String teaName;

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
    private String title;

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
    private String phone;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}