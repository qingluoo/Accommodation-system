package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.repairer.RepairerQueryRequest;
import com.scu.Accommodation.model.entity.Repairer;
import com.scu.Accommodation.model.vo.RepairerVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 维修员服务
 *
 */
public interface RepairerService extends IService<Repairer> {

    /**
     * 校验数据
     *
     * @param repairer
     * @param add 对创建的数据进行校验
     */
    void validRepairer(Repairer repairer, boolean add);

    /**
     * 获取查询条件
     *
     * @param repairerQueryRequest
     * @return
     */
    QueryWrapper<Repairer> getQueryWrapper(RepairerQueryRequest repairerQueryRequest);
    
    /**
     * 获取维修员封装
     *
     * @param repairer
     * @param request
     * @return
     */
    RepairerVO getRepairerVO(Repairer repairer, HttpServletRequest request);

    /**
     * 分页获取维修员封装
     *
     * @param repairerPage
     * @param request
     * @return
     */
    Page<RepairerVO> getRepairerVOPage(Page<Repairer> repairerPage, HttpServletRequest request);
}
