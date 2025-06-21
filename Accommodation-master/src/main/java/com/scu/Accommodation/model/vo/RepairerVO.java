package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Repairer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 维修员视图
 *
 */
@Data
public class RepairerVO implements Serializable {

    /**
     * id
     */
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


    /**
     * 封装类转对象
     *
     * @param repairerVO
     * @return
     */
    public static Repairer voToObj(RepairerVO repairerVO) {
        if (repairerVO == null) {
            return null;
        }
        Repairer repairer = new Repairer();
        BeanUtils.copyProperties(repairerVO, repairer);
        return repairer;
    }

    /**
     * 对象转封装类
     *
     * @param repairer
     * @return
     */
    public static RepairerVO objToVo(Repairer repairer) {
        if (repairer == null) {
            return null;
        }
        RepairerVO repairerVO = new RepairerVO();
        BeanUtils.copyProperties(repairer, repairerVO);
        return repairerVO;
    }
}
