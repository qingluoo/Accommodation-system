package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerQueryRequest;
import com.scu.Accommodation.model.entity.Housemanager;
import com.scu.Accommodation.model.vo.HouseManagerVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 宿舍总管服务
 *
 */
public interface HouseManagerService extends IService<Housemanager> {

    /**
     * 校验数据
     *
     * @param housemanager
     * @param add 对创建的数据进行校验
     */
    void validHouseManager(Housemanager housemanager, boolean add);

    /**
     * 获取查询条件
     *
     * @param housemanagerQueryRequest
     * @return
     */
    QueryWrapper<Housemanager> getQueryWrapper(HouseManagerQueryRequest housemanagerQueryRequest);
    
    /**
     * 获取宿舍总管封装
     *
     * @param housemanager
     * @param request
     * @return
     */
    HouseManagerVO getHouseManagerVO(Housemanager housemanager, HttpServletRequest request);

    /**
     * 分页获取宿舍总管封装
     *
     * @param housemanagerPage
     * @param request
     * @return
     */
    Page<HouseManagerVO> getHouseManagerVOPage(Page<Housemanager> housemanagerPage, HttpServletRequest request);
}
