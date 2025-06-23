package com.scu.Accommodation.model.dto.transaction;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑业务请求
 *
 */
@Data
public class TransactionEditRequest implements Serializable {

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

    /**
     *
     */
    private String reason;

    private Long userId;

    private static final long serialVersionUID = 1L;
}