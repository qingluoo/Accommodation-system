package com.scu.Accommodation.model.vo;

import com.scu.Accommodation.model.entity.Houseparents;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 宿管视图
 *
 */
@Data
public class HouseparentsVO implements Serializable {

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
     * @param houseparentsVO
     * @return
     */
    public static Houseparents voToObj(HouseparentsVO houseparentsVO) {
        if (houseparentsVO == null) {
            return null;
        }
        Houseparents houseparents = new Houseparents();
        BeanUtils.copyProperties(houseparentsVO, houseparents);
        return houseparents;
    }

    /**
     * 对象转封装类
     *
     * @param houseparents
     * @return
     */
    public static HouseparentsVO objToVo(Houseparents houseparents) {
        if (houseparents == null) {
            return null;
        }
        HouseparentsVO houseparentsVO = new HouseparentsVO();
        BeanUtils.copyProperties(houseparents, houseparentsVO);
        return houseparentsVO;
    }
}
