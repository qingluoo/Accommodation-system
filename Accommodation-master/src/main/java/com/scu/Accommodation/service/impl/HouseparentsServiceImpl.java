package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.HouseparentsMapper;
import com.scu.Accommodation.model.dto.houseparents.HouseparentsQueryRequest;
import com.scu.Accommodation.model.entity.Houseparents;
import com.scu.Accommodation.model.vo.HouseparentsVO;
import com.scu.Accommodation.service.HouseparentsService;
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
 * 宿管服务实现
 *
 */
@Service
@Slf4j
public class HouseparentsServiceImpl extends ServiceImpl<HouseparentsMapper, Houseparents> implements HouseparentsService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param houseparents
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validHouseparents(Houseparents houseparents, boolean add) {
        ThrowUtils.throwIf(houseparents == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = houseparents.getUnionId();
        String name = houseparents.getName();
        String phone = houseparents.getPhone();
        String park = houseparents.getPark();
        String building = houseparents.getBuilding();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(phone), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(park), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(building), ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param houseparentsQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Houseparents> getQueryWrapper(HouseparentsQueryRequest houseparentsQueryRequest) {
        QueryWrapper<Houseparents> queryWrapper = new QueryWrapper<>();
        if (houseparentsQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = houseparentsQueryRequest.getId();
        String unionId = houseparentsQueryRequest.getUnionId();
        String name = houseparentsQueryRequest.getName();
        String phone = houseparentsQueryRequest.getPhone();
        String park = houseparentsQueryRequest.getPark();
        String building = houseparentsQueryRequest.getBuilding();
        String sortField = houseparentsQueryRequest.getSortField();
        String sortOrder = houseparentsQueryRequest.getSortOrder();
        Long userId = houseparentsQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 模糊
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(building), "building", building);
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(unionId), "unionId", unionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(phone), "phone", phone);
        queryWrapper.eq(ObjectUtils.isNotEmpty(park), "park", park);
        queryWrapper.eq(ObjectUtils.isNotEmpty(building), "building", building);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取宿管封装
     *
     * @param houseparents
     * @param request
     * @return
     */
    @Override
    public HouseparentsVO getHouseparentsVO(Houseparents houseparents, HttpServletRequest request) {
        // 对象转封装类
        HouseparentsVO houseparentsVO = HouseparentsVO.objToVo(houseparents);

        return houseparentsVO;
    }

    /**
     * 分页获取宿管封装
     *
     * @param houseparentsPage
     * @param request
     * @return
     */
    @Override
    public Page<HouseparentsVO> getHouseparentsVOPage(Page<Houseparents> houseparentsPage, HttpServletRequest request) {
        List<Houseparents> houseparentsList = houseparentsPage.getRecords();
        Page<HouseparentsVO> houseparentsVOPage = new Page<>(houseparentsPage.getCurrent(), houseparentsPage.getSize(), houseparentsPage.getTotal());
        if (CollUtil.isEmpty(houseparentsList)) {
            return houseparentsVOPage;
        }
        // 对象列表 => 封装对象列表
        List<HouseparentsVO> houseparentsVOList = houseparentsList.stream().map(houseparents -> {
            return HouseparentsVO.objToVo(houseparents);
        }).collect(Collectors.toList());

        houseparentsVOPage.setRecords(houseparentsVOList);
        return houseparentsVOPage;
    }

}
