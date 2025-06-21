package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Apartment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

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
