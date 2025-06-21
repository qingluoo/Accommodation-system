package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.RepairerMapper;
import com.scu.Accommodation.model.dto.repairer.RepairerQueryRequest;
import com.scu.Accommodation.model.entity.Repairer;
import com.scu.Accommodation.model.vo.RepairerVO;
import com.scu.Accommodation.service.RepairerService;
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
 * 维修员服务实现
 *
 */
@Service
@Slf4j
public class RepairerServiceImpl extends ServiceImpl<RepairerMapper, Repairer> implements RepairerService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param repairer
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validRepairer(Repairer repairer, boolean add) {
        ThrowUtils.throwIf(repairer == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = repairer.getUnionId();
        String name = repairer.getName();
        String phone = repairer.getPhone();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(phone), ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param repairerQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Repairer> getQueryWrapper(RepairerQueryRequest repairerQueryRequest) {
        QueryWrapper<Repairer> queryWrapper = new QueryWrapper<>();
        if (repairerQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = repairerQueryRequest.getId();
        String unionId = repairerQueryRequest.getUnionId();
        String name = repairerQueryRequest.getName();
        String phone = repairerQueryRequest.getPhone();
        Long userId = repairerQueryRequest.getUserId();
        String sortField = repairerQueryRequest.getSortField();
        String sortOrder = repairerQueryRequest.getSortOrder();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(ObjectUtils.isNotEmpty(unionId), "unionId", unionId);
        queryWrapper.like(ObjectUtils.isNotEmpty(name), "name", name);
        queryWrapper.like(ObjectUtils.isNotEmpty(phone), "phone", phone);
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
     * 获取维修员封装
     *
     * @param repairer
     * @param request
     * @return
     */
    @Override
    public RepairerVO getRepairerVO(Repairer repairer, HttpServletRequest request) {
        // 对象转封装类
        RepairerVO repairerVO = RepairerVO.objToVo(repairer);

        return repairerVO;
    }

    /**
     * 分页获取维修员封装
     *
     * @param repairerPage
     * @param request
     * @return
     */
    @Override
    public Page<RepairerVO> getRepairerVOPage(Page<Repairer> repairerPage, HttpServletRequest request) {
        List<Repairer> repairerList = repairerPage.getRecords();
        Page<RepairerVO> repairerVOPage = new Page<>(repairerPage.getCurrent(), repairerPage.getSize(), repairerPage.getTotal());
        if (CollUtil.isEmpty(repairerList)) {
            return repairerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<RepairerVO> repairerVOList = repairerList.stream().map(repairer -> {
            return RepairerVO.objToVo(repairer);
        }).collect(Collectors.toList());

        repairerVOPage.setRecords(repairerVOList);
        return repairerVOPage;
    }

}
