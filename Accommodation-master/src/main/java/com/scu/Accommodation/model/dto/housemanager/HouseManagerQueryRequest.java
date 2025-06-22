package com.scu.Accommodation.model.dto.housemanager;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 查询宿舍总管请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HouseManagerQueryRequest extends PageRequest implements Serializable {

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


    private Integer isDelete;


    private Long userId;


    private static final long serialVersionUID = 1L;

}