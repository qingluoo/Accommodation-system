package com.scu.Accommodation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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

    /**
     * 
     */
    private String roomId;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}