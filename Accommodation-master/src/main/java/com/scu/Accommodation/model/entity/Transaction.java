package com.scu.Accommodation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName transaction
 */
@TableName(value ="transaction")
@Data
public class Transaction {
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
    private String name;


    private Integer grade;


    private String college;


    private String major;

    /**
     * 
     */
    private Integer roleId;

    /**
     * 
     */
    private String description;

    private String filename;

    private byte[] filebyte;

    /**
     * 
     */
    private Integer type;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private String reason;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;
}