package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.ApartmentMapper;
import com.scu.Accommodation.model.dto.apartment.ApartmentQueryRequest;
import com.scu.Accommodation.model.entity.Apartment;
import com.scu.Accommodation.model.vo.ApartmentVO;
import com.scu.Accommodation.service.ApartmentService;
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
 * 公寓服务实现
 *
 */
@Service
@Slf4j
public class ApartmentServiceImpl extends ServiceImpl<ApartmentMapper, Apartment> implements ApartmentService {

    /**
     * 校验数据
     *
     * @param apartment
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validApartment(Apartment apartment, boolean add) {
        ThrowUtils.throwIf(apartment == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String park = apartment.getPark();
        String building = apartment.getBuilding();
        String room = apartment.getRoom();
        Integer bedNum = apartment.getBedNum();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(park), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(building), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(room), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(bedNum == null || bedNum <= 0, ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param apartmentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Apartment> getQueryWrapper(ApartmentQueryRequest apartmentQueryRequest) {
        QueryWrapper<Apartment> queryWrapper = new QueryWrapper<>();
        if (apartmentQueryRequest == null) {
            return queryWrapper;
        }
        // 从请求对象中取值
        String park = apartmentQueryRequest.getPark();
        String building = apartmentQueryRequest.getBuilding();
        String room = apartmentQueryRequest.getRoom();
        Integer bedNum = apartmentQueryRequest.getBedNum();
        Integer liveNum = apartmentQueryRequest.getLiveNum();
        String roomType = apartmentQueryRequest.getRoomType();
        Integer isFull = apartmentQueryRequest.getIsFull();

        String sortField = apartmentQueryRequest.getSortField();
        String sortOrder = apartmentQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(park), "park", park);
        queryWrapper.like(StringUtils.isNotBlank(building), "building", building);
        queryWrapper.like(StringUtils.isNotBlank(room), "room", room);
        queryWrapper.like(StringUtils.isNotBlank(roomType), "roomType", roomType);
        queryWrapper.like(ObjectUtils.isNotEmpty(liveNum), "liveNum", liveNum);

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(bedNum), "bedNum", bedNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(liveNum), "liveNum", liveNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isFull), "isFull", isFull);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取公寓封装
     *
     * @param apartment
     * @param request
     * @return
     */
    @Override
    public ApartmentVO getApartmentVO(Apartment apartment, HttpServletRequest request) {
        // 对象转封装类
        ApartmentVO apartmentVO = ApartmentVO.objToVo(apartment);
        return apartmentVO;
    }

    /**
     * 分页获取公寓封装
     *
     * @param apartmentPage
     * @param request
     * @return
     */
    @Override
    public Page<ApartmentVO> getApartmentVOPage(Page<Apartment> apartmentPage, HttpServletRequest request) {
        List<Apartment> apartmentList = apartmentPage.getRecords();
        Page<ApartmentVO> apartmentVOPage = new Page<>(apartmentPage.getCurrent(), apartmentPage.getSize(), apartmentPage.getTotal());
        if (CollUtil.isEmpty(apartmentList)) {
            return apartmentVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ApartmentVO> apartmentVOList = apartmentList.stream().map(apartment -> {
            return ApartmentVO.objToVo(apartment);
        }).collect(Collectors.toList());

        apartmentVOPage.setRecords(apartmentVOList);
        return apartmentVOPage;
    }

}
