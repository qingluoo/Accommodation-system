package com.scu.Accommodation.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.model.dto.role.RoleAddRequest;
import com.scu.Accommodation.model.dto.role.RoleEditRequest;
import com.scu.Accommodation.model.dto.role.RoleQueryRequest;
import com.scu.Accommodation.model.dto.role.RoleUpdateRequest;
import com.scu.Accommodation.model.entity.Role;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.RoleVO;
import com.scu.Accommodation.service.RoleService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 角色接口
 *
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建角色
     *
     * @param roleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRole(@RequestBody RoleAddRequest roleAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(roleAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Role role = new Role();
        BeanUtils.copyProperties(roleAddRequest, role);
        role.setCodelist(JSONUtil.toJsonStr(roleAddRequest.getCodelist()));
        // 数据校验
        roleService.validRole(role, true);
        // todo 填充默认值
        // 写入数据库
        boolean result = roleService.save(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newRoleId = role.getId();
        return ResultUtils.success(newRoleId);
    }

    /**
     * 删除角色
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRole(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Role oldRole = roleService.getById(id);
        ThrowUtils.throwIf(oldRole == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = roleService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新角色（仅管理员可用）
     *
     * @param roleUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        if (roleUpdateRequest == null || roleUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateRequest, role);
        role.setCodelist(JSONUtil.toJsonStr(roleUpdateRequest.getCodelist()));
        // 数据校验
        roleService.validRole(role, false);
        // 判断是否存在
        long id = roleUpdateRequest.getId();
        Role oldRole = roleService.getById(id);
        ThrowUtils.throwIf(oldRole == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = roleService.updateById(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取角色（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<RoleVO> getRoleVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Role role = roleService.getById(id);
        ThrowUtils.throwIf(role == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(roleService.getRoleVO(role, request));
    }

    /**
     * 分页获取角色列表（仅管理员可用）
     *
     * @param roleQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Role>> listRoleByPage(@RequestBody RoleQueryRequest roleQueryRequest) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        // 查询数据库
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        return ResultUtils.success(rolePage);
    }

    /**
     * 分页获取角色列表（封装类）
     *
     * @param roleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RoleVO>> listRoleVOByPage(@RequestBody RoleQueryRequest roleQueryRequest,
                                                               HttpServletRequest request) {
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        // 查询数据库
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        // 获取封装类
        return ResultUtils.success(roleService.getRoleVOPage(rolePage, request));
    }

    /**
     * 分页获取当前登录用户创建的角色列表
     *
     * @param roleQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<RoleVO>> listMyRoleVOByPage(@RequestBody RoleQueryRequest roleQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(roleQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        roleQueryRequest.setUserId(loginUser.getId());
        long current = roleQueryRequest.getCurrent();
        long size = roleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Role> rolePage = roleService.page(new Page<>(current, size),
                roleService.getQueryWrapper(roleQueryRequest));
        // 获取封装类
        return ResultUtils.success(roleService.getRoleVOPage(rolePage, request));
    }

    /**
     * 编辑角色（给用户使用）
     *
     * @param roleEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editRole(@RequestBody RoleEditRequest roleEditRequest, HttpServletRequest request) {
        if (roleEditRequest == null || roleEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Role role = new Role();
        BeanUtils.copyProperties(roleEditRequest, role);
        // 数据校验
        roleService.validRole(role, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = roleEditRequest.getId();
        Role oldRole = roleService.getById(id);
        ThrowUtils.throwIf(oldRole == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = roleService.updateById(role);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
