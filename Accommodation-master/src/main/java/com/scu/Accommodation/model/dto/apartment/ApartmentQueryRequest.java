package com.scu.Accommodation.model.dto.apartment;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询公寓请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApartmentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private String park;

    /**
     * 搜索词
     */
    private String building;

    /**
     * 房间
     */
    private String room;

    /**
     * 床号
     */
    private Integer bedNum;

    /**
     * 人数
     */
    private Integer liveNum;

    /**
     * 房间类型
     */
    private String roomType;

    /**
     * 是否满员
     */
    private Integer isFull;


    /**
     * 排序
     */
    private String sortField;


    /**
     * 创建用户 id
     */
    private Long userId;
        
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}