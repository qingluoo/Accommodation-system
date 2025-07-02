package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Role;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色视图
 *
 */
@Data
public class RoleVO implements Serializable {

    /**
     * 标题
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;


    /**
     * 权限列表
     */
    private List<String> codelist;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 封装类转对象
     *
     * @param roleVO
     * @return
     */
    public static Role voToObj(RoleVO roleVO) {
        if (roleVO == null) {
            return null;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleVO, role);
        List<String> codelist = roleVO.getCodelist();
        role.setCodelist(JSONUtil.toJsonStr(codelist));
        return role;
    }

    /**
     * 对象转封装类
     *
     * @param role
     * @return
     */
    public static RoleVO objToVo(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        roleVO.setCodelist(JSONUtil.toList(role.getCodelist(), String.class));
        return roleVO;
    }
}
