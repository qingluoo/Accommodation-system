package com.scu.Accommodation.model.dto.repairer;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询维修员请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RepairerQueryRequest extends PageRequest implements Serializable {

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