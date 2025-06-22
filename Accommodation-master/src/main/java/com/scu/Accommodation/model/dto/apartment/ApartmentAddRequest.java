package com.scu.Accommodation.model.dto.apartment;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建公寓请求
 *
 */
@Data
public class ApartmentAddRequest implements Serializable {

    private Long id;
    /**
     * 园区
     */
    private String park;

    /**
     * 楼栋
     */
    private String building;

    /**
     * 房间
     */
    private String room;

    /**
     * 床位数
     */
    private Integer bedNum;

    /**
     * 居住人数
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


    private Integer isDelete;

    private Long userId;

    private static final long serialVersionUID = 1L;
}