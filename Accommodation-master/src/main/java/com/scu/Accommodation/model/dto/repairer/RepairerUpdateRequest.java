package com.scu.Accommodation.model.dto.repairer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新维修员请求
 *
 */
@Data
public class RepairerUpdateRequest implements Serializable {

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
     * 标签列表
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}