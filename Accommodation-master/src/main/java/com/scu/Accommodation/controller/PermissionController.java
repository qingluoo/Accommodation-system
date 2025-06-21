package com.scu.Accommodation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.model.dto.permission.PermissionAddRequest;
import com.scu.Accommodation.model.dto.permission.PermissionEditRequest;
import com.scu.Accommodation.model.dto.permission.PermissionQueryRequest;
import com.scu.Accommodation.model.dto.permission.PermissionUpdateRequest;
import com.scu.Accommodation.model.entity.Permission;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.PermissionVO;
import com.scu.Accommodation.service.PermissionService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限接口
 *
 */
@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建权限
     *
     * @param permissionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPermission(@RequestBody PermissionAddRequest permissionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(permissionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionAddRequest, permission);
        // 数据校验
        permissionService.validPermission(permission, true);
        // 写入数据库
        boolean result = permissionService.save(permission);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newPermissionId = permission.getId();
        return ResultUtils.success(newPermissionId);
    }

    /**
     * 删除权限
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePermission(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Permission oldPermission = permissionService.getById(id);
        ThrowUtils.throwIf(oldPermission == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = permissionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新权限（仅管理员可用）
     *
     * @param permissionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePermission(@RequestBody PermissionUpdateRequest permissionUpdateRequest) {
        if (permissionUpdateRequest == null || permissionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionUpdateRequest, permission);
        // 数据校验
        permissionService.validPermission(permission, false);
        // 判断是否存在
        long id = permissionUpdateRequest.getId();
        Permission oldPermission = permissionService.getById(id);
        ThrowUtils.throwIf(oldPermission == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = permissionService.updateById(permission);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取权限（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<PermissionVO> getPermissionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Permission permission = permissionService.getById(id);
        ThrowUtils.throwIf(permission == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(permissionService.getPermissionVO(permission, request));
    }

    /**
     * 分页获取权限列表（仅管理员可用）
     *
     * @param permissionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Permission>> listPermissionByPage(@RequestBody PermissionQueryRequest permissionQueryRequest) {
        long current = permissionQueryRequest.getCurrent();
        long size = permissionQueryRequest.getPageSize();
        // 查询数据库
        Page<Permission> permissionPage = permissionService.page(new Page<>(current, size),
                permissionService.getQueryWrapper(permissionQueryRequest));
        return ResultUtils.success(permissionPage);
    }

    /**
     * 分页获取权限列表（封装类）
     *
     * @param permissionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PermissionVO>> listPermissionVOByPage(@RequestBody PermissionQueryRequest permissionQueryRequest,
                                                               HttpServletRequest request) {
        long current = permissionQueryRequest.getCurrent();
        long size = permissionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Permission> permissionPage = permissionService.page(new Page<>(current, size),
                permissionService.getQueryWrapper(permissionQueryRequest));
        // 获取封装类
        return ResultUtils.success(permissionService.getPermissionVOPage(permissionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的权限列表
     *
     * @param permissionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<PermissionVO>> listMyPermissionVOByPage(@RequestBody PermissionQueryRequest permissionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(permissionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        permissionQueryRequest.setUserId(loginUser.getId());
        long current = permissionQueryRequest.getCurrent();
        long size = permissionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Permission> permissionPage = permissionService.page(new Page<>(current, size),
                permissionService.getQueryWrapper(permissionQueryRequest));
        // 获取封装类
        return ResultUtils.success(permissionService.getPermissionVOPage(permissionPage, request));
    }

    /**
     * 编辑权限（给用户使用）
     *
     * @param permissionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editPermission(@RequestBody PermissionEditRequest permissionEditRequest, HttpServletRequest request) {
        if (permissionEditRequest == null || permissionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionEditRequest, permission);
        // 数据校验
        permissionService.validPermission(permission, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = permissionEditRequest.getId();
        Permission oldPermission = permissionService.getById(id);
        ThrowUtils.throwIf(oldPermission == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = permissionService.updateById(permission);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
