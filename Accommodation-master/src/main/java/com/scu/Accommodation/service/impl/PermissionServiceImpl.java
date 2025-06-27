package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.PermissionMapper;
import com.scu.Accommodation.model.dto.permission.PermissionQueryRequest;
import com.scu.Accommodation.model.entity.Permission;
import com.scu.Accommodation.model.vo.PermissionVO;
import com.scu.Accommodation.service.PermissionService;
import com.scu.Accommodation.service.UserService;
import com.scu.Accommodation.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 *
 */
@Service
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param permission
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validPermission(Permission permission, boolean add) {
        ThrowUtils.throwIf(permission == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String name = permission.getName();
        String code = permission.getCode();
        String description = permission.getDescription();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(code), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(description), ErrorCode.PARAMS_ERROR);
        }

    }

    /**
     * 获取查询条件
     *
     * @param permissionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Permission> getQueryWrapper(PermissionQueryRequest permissionQueryRequest) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (permissionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String name = permissionQueryRequest.getName();
        String code = permissionQueryRequest.getCode();
        String description = permissionQueryRequest.getDescription();
        String sortField = permissionQueryRequest.getSortField();
        String sortOrder = permissionQueryRequest.getSortOrder();
        // todo 补充需要的查询条件
        // 从多字段中搜索

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(code), "code", code);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询

        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取权限封装
     *
     * @param permission
     * @param request
     * @return
     */
    @Override
    public PermissionVO getPermissionVO(Permission permission, HttpServletRequest request) {
        // 对象转封装类
        PermissionVO permissionVO = PermissionVO.objToVo(permission);

        return permissionVO;
    }

    /**
     * 分页获取权限封装
     *
     * @param permissionPage
     * @param request
     * @return
     */
    @Override
    public Page<PermissionVO> getPermissionVOPage(Page<Permission> permissionPage, HttpServletRequest request) {
        List<Permission> permissionList = permissionPage.getRecords();
        Page<PermissionVO> permissionVOPage = new Page<>(permissionPage.getCurrent(), permissionPage.getSize(), permissionPage.getTotal());
        if (CollUtil.isEmpty(permissionList)) {
            return permissionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<PermissionVO> permissionVOList = permissionList.stream().map(permission -> {
            return PermissionVO.objToVo(permission);
        }).collect(Collectors.toList());


        permissionVOPage.setRecords(permissionVOList);
        return permissionVOPage;
    }

}
