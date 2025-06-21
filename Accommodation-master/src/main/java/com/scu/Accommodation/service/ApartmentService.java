package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.apartment.ApartmentQueryRequest;
import com.scu.Accommodation.model.entity.Apartment;
import com.scu.Accommodation.model.vo.ApartmentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 公寓服务
 *
 */
public interface ApartmentService extends IService<Apartment> {

    /**
     * 校验数据
     *
     * @param apartment
     * @param add 对创建的数据进行校验
     */
    void validApartment(Apartment apartment, boolean add);

    /**
     * 获取查询条件
     *
     * @param apartmentQueryRequest
     * @return
     */
    QueryWrapper<Apartment> getQueryWrapper(ApartmentQueryRequest apartmentQueryRequest);
    
    /**
     * 获取公寓封装
     *
     * @param apartment
     * @param request
     * @return
     */
    ApartmentVO getApartmentVO(Apartment apartment, HttpServletRequest request);

    /**
     * 分页获取公寓封装
     *
     * @param apartmentPage
     * @param request
     * @return
     */
    Page<ApartmentVO> getApartmentVOPage(Page<Apartment> apartmentPage, HttpServletRequest request);
}
