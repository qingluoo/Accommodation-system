package com.scu.Accommodation.model.dto.transaction;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询业务请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionQueryRequest extends PageRequest implements Serializable {

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


    private Long userId;


    private static final long serialVersionUID = 1L;
}