package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.RoleMapper;
import com.scu.Accommodation.model.dto.role.RoleQueryRequest;
import com.scu.Accommodation.model.entity.Role;
import com.scu.Accommodation.model.vo.RoleVO;
import com.scu.Accommodation.service.RoleService;
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
 * 角色服务实现
 *
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param role
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validRole(Role role, boolean add) {
        ThrowUtils.throwIf(role == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String name = role.getName();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param roleQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Role> getQueryWrapper(RoleQueryRequest roleQueryRequest) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (roleQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = roleQueryRequest.getId();
        String name = roleQueryRequest.getName();
        List<String> codelist = roleQueryRequest.getCodelist();
        String sortField = roleQueryRequest.getSortField();
        String sortOrder = roleQueryRequest.getSortOrder();
        Long userId = roleQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(codelist)) {
            for (String code : codelist) {
                queryWrapper.like("codelist", "\"" + code + "\"");
            }
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取角色封装
     *
     * @param role
     * @param request
     * @return
     */
    @Override
    public RoleVO getRoleVO(Role role, HttpServletRequest request) {
        // 对象转封装类
        RoleVO roleVO = RoleVO.objToVo(role);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        return roleVO;
    }

    /**
     * 分页获取角色封装
     *
     * @param rolePage
     * @param request
     * @return
     */
    @Override
    public Page<RoleVO> getRoleVOPage(Page<Role> rolePage, HttpServletRequest request) {
        List<Role> roleList = rolePage.getRecords();
        Page<RoleVO> roleVOPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(), rolePage.getTotal());
        if (CollUtil.isEmpty(roleList)) {
            return roleVOPage;
        }
        // 对象列表 => 封装对象列表
        List<RoleVO> roleVOList = roleList.stream().map(role -> {
            return RoleVO.objToVo(role);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除

        roleVOPage.setRecords(roleVOList);
        return roleVOPage;
    }

}
