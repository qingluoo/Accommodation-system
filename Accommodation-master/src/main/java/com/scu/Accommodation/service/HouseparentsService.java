package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.houseparents.HouseparentsQueryRequest;
import com.scu.Accommodation.model.entity.Houseparents;
import com.scu.Accommodation.model.vo.HouseparentsVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 宿管服务
 *
 */
public interface HouseparentsService extends IService<Houseparents> {

    /**
     * 校验数据
     *
     * @param houseparents
     * @param add 对创建的数据进行校验
     */
    void validHouseparents(Houseparents houseparents, boolean add);

    /**
     * 获取查询条件
     *
     * @param houseparentsQueryRequest
     * @return
     */
    QueryWrapper<Houseparents> getQueryWrapper(HouseparentsQueryRequest houseparentsQueryRequest);
    
    /**
     * 获取宿管封装
     *
     * @param houseparents
     * @param request
     * @return
     */
    HouseparentsVO getHouseparentsVO(Houseparents houseparents, HttpServletRequest request);

    /**
     * 分页获取宿管封装
     *
     * @param houseparentsPage
     * @param request
     * @return
     */
    Page<HouseparentsVO> getHouseparentsVOPage(Page<Houseparents> houseparentsPage, HttpServletRequest request);
}
