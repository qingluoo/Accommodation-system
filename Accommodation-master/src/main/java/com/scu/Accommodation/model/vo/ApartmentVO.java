package com.scu.Accommodation.model.vo;


import com.scu.Accommodation.model.entity.Apartment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;


/**
 * 公寓视图
 *
 */
@Data
public class ApartmentVO implements Serializable {

    /**
     * id
     */
    private Long id;

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
     * 房间类型
     */
    private String roomType;

    /**
     * 床位数
     */
    private Integer bedNum;

    /**
     * 居住人数
     */
    private Integer liveNum;

    /**
     * 是否满床
     */
    private Integer isFull;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;



    /**
     * 封装类转对象
     *
     * @param apartmentVO
     * @return
     */
    public static Apartment voToObj(ApartmentVO apartmentVO) {
        if (apartmentVO == null) {
            return null;
        }
        Apartment apartment = new Apartment();
        BeanUtils.copyProperties(apartmentVO, apartment);
        return apartment;
    }

    /**
     * 对象转封装类
     *
     * @param apartment
     * @return
     */
    public static ApartmentVO objToVo(Apartment apartment) {
        if (apartment == null) {
            return null;
        }
        ApartmentVO apartmentVO = new ApartmentVO();
        BeanUtils.copyProperties(apartment, apartmentVO);
        return apartmentVO;
    }
}
