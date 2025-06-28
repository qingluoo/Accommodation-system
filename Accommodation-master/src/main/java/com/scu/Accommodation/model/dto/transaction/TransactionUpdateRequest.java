package com.scu.Accommodation.model.dto.transaction;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新业务请求
 *
 */
@Data
public class TransactionUpdateRequest implements Serializable {

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

    private Long userId;

    private static final long serialVersionUID = 1L;
}