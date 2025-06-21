package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Permission;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 权限视图
 *
 */
@Data
public class PermissionVO implements Serializable {

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
     * @param permissionVO
     * @return
     */
    public static Permission voToObj(PermissionVO permissionVO) {
        if (permissionVO == null) {
            return null;
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVO, permission);
        return permission;
    }

    /**
     * 对象转封装类
     *
     * @param permission
     * @return
     */
    public static PermissionVO objToVo(Permission permission) {
        if (permission == null) {
            return null;
        }
        PermissionVO permissionVO = new PermissionVO();
        BeanUtils.copyProperties(permission, permissionVO);
        return permissionVO;
    }
}
