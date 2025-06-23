package com.scu.Accommodation.model.dto.repairer;

import com.scu.Accommodation.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建维修员请求
 *
 */
@Data
public class RepairerAddRequest implements Serializable {

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

    private Long userId;

    private static final long serialVersionUID = 1L;
}