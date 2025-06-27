package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.role.RoleQueryRequest;
import com.scu.Accommodation.model.entity.Role;
import com.scu.Accommodation.model.vo.RoleVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色服务
 *
 */
public interface RoleService extends IService<Role> {

    /**
     * 校验数据
     *
     * @param role
     * @param add 对创建的数据进行校验
     */
    void validRole(Role role, boolean add);

    /**
     * 获取查询条件
     *
     * @param roleQueryRequest
     * @return
     */
    QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest);
    
    /**
     * 获取角色封装
     *
     * @param role
     * @param request
     * @return
     */
    RoleVO getRoleVO(Role role, HttpServletRequest request);

    /**
     * 分页获取角色封装
     *
     * @param rolePage
     * @param request
     * @return
     */
    Page<RoleVO> getRoleVOPage(Page<Role> rolePage, HttpServletRequest request);
}
