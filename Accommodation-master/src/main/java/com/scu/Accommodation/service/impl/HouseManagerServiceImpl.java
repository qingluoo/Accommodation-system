package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.HousemanagerMapper;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerQueryRequest;
import com.scu.Accommodation.model.entity.Housemanager;
import com.scu.Accommodation.model.vo.HouseManagerVO;
import com.scu.Accommodation.service.HouseManagerService;
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
 * 宿舍总管服务实现
 *
 */
@Service
@Slf4j
public class HouseManagerServiceImpl extends ServiceImpl<HousemanagerMapper, Housemanager> implements HouseManagerService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param housemanager
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validHouseManager(Housemanager housemanager, boolean add) {
        ThrowUtils.throwIf(housemanager == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = housemanager.getUnionId();
        String name = housemanager.getName();
        String phone = housemanager.getPhone();
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
     * @param housemanagerQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Housemanager> getQueryWrapper(HouseManagerQueryRequest housemanagerQueryRequest) {
        QueryWrapper<Housemanager> queryWrapper = new QueryWrapper<>();
        if (housemanagerQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String unionId = housemanagerQueryRequest.getUnionId();
        String name = housemanagerQueryRequest.getName();
        String phone = housemanagerQueryRequest.getPhone();
        String sortField = housemanagerQueryRequest.getSortField();
        String sortOrder = housemanagerQueryRequest.getSortOrder();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(phone), "phone", phone);

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(phone), "phone", housemanagerQueryRequest.getPhone());
        queryWrapper.eq(ObjectUtils.isNotEmpty(unionId), "unionId", housemanagerQueryRequest.getUnionId());
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取宿舍总管封装
     *
     * @param housemanager
     * @param request
     * @return
     */
    @Override
    public HouseManagerVO getHouseManagerVO(Housemanager housemanager, HttpServletRequest request) {
        // 对象转封装类
        HouseManagerVO housemanagerVO = HouseManagerVO.objToVo(housemanager);

        return housemanagerVO;
    }

    /**
     * 分页获取宿舍总管封装
     *
     * @param housemanagerPage
     * @param request
     * @return
     */
    @Override
    public Page<HouseManagerVO> getHouseManagerVOPage(Page<Housemanager> housemanagerPage, HttpServletRequest request) {
        List<Housemanager> housemanagerList = housemanagerPage.getRecords();
        Page<HouseManagerVO> housemanagerVOPage = new Page<>(housemanagerPage.getCurrent(), housemanagerPage.getSize(), housemanagerPage.getTotal());
        if (CollUtil.isEmpty(housemanagerList)) {
            return housemanagerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<HouseManagerVO> housemanagerVOList = housemanagerList.stream().map(housemanager -> {
            return HouseManagerVO.objToVo(housemanager);
        }).collect(Collectors.toList());


        housemanagerVOPage.setRecords(housemanagerVOList);
        return housemanagerVOPage;
    }

}
