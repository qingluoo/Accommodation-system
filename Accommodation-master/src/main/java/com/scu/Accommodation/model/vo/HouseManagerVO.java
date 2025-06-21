package com.scu.Accommodation.model.vo;

import com.scu.Accommodation.model.entity.Housemanager;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 宿舍总管视图
 *
 */
@Data
public class HouseManagerVO implements Serializable {

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
     * @param housemanagerVO
     * @return
     */
    public static Housemanager voToObj(HouseManagerVO housemanagerVO) {
        if (housemanagerVO == null) {
            return null;
        }
        Housemanager housemanager = new Housemanager();
        BeanUtils.copyProperties(housemanagerVO, housemanager);
        return housemanager;
    }

    /**
     * 对象转封装类
     *
     * @param housemanager
     * @return
     */
    public static HouseManagerVO objToVo(Housemanager housemanager) {
        if (housemanager == null) {
            return null;
        }
        HouseManagerVO housemanagerVO = new HouseManagerVO();
        BeanUtils.copyProperties(housemanager, housemanagerVO);
        return housemanagerVO;
    }
}
