package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.permission.PermissionQueryRequest;
import com.scu.Accommodation.model.entity.Permission;
import com.scu.Accommodation.model.vo.PermissionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限服务
 *
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 校验数据
     *
     * @param permission
     * @param add 对创建的数据进行校验
     */
    void validPermission(Permission permission, boolean add);

    /**
     * 获取查询条件
     *
     * @param permissionQueryRequest
     * @return
     */
    QueryWrapper<Permission> getQueryWrapper(PermissionQueryRequest permissionQueryRequest);
    
    /**
     * 获取权限封装
     *
     * @param permission
     * @param request
     * @return
     */
    PermissionVO getPermissionVO(Permission permission, HttpServletRequest request);

    /**
     * 分页获取权限封装
     *
     * @param permissionPage
     * @param request
     * @return
     */
    Page<PermissionVO> getPermissionVOPage(Page<Permission> permissionPage, HttpServletRequest request);
}
