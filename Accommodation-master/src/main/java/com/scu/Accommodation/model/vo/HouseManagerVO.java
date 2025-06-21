package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.HouseManager;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 宿舍总管视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
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
    public static HouseManager voToObj(HouseManagerVO housemanagerVO) {
        if (housemanagerVO == null) {
            return null;
        }
        HouseManager housemanager = new HouseManager();
        BeanUtils.copyProperties(housemanagerVO, housemanager);
        return housemanager;
    }

    /**
     * 对象转封装类
     *
     * @param housemanager
     * @return
     */
    public static HouseManagerVO objToVo(HouseManager housemanager) {
        if (housemanager == null) {
            return null;
        }
        HouseManagerVO housemanagerVO = new HouseManagerVO();
        BeanUtils.copyProperties(housemanager, housemanagerVO);
        return housemanagerVO;
    }
}
