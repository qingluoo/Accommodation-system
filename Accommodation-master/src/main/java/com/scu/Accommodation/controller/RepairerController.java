package com.scu.Accommodation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.model.dto.repairer.RepairerAddRequest;
import com.scu.Accommodation.model.dto.repairer.RepairerEditRequest;
import com.scu.Accommodation.model.dto.repairer.RepairerQueryRequest;
import com.scu.Accommodation.model.dto.repairer.RepairerUpdateRequest;
import com.scu.Accommodation.model.entity.Repairer;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.RepairerVO;
import com.scu.Accommodation.service.RepairerService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 维修员接口
 *
 */
@RestController
@RequestMapping("/repairer")
@Slf4j
public class RepairerController {

    @Resource
    private RepairerService repairerService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建维修员
     *
     * @param repairerAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRepairer(@RequestBody RepairerAddRequest repairerAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(repairerAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Repairer repairer = new Repairer();
        BeanUtils.copyProperties(repairerAddRequest, repairer);
        // 数据校验
        repairerService.validRepairer(repairer, true);
        // 写入数据库
        boolean result = repairerService.save(repairer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newRepairerId = repairer.getId();
        return ResultUtils.success(newRepairerId);
    }

    /**
     * 删除维修员
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRepairer(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Repairer oldRepairer = repairerService.getById(id);
        ThrowUtils.throwIf(oldRepairer == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = repairerService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新维修员（仅管理员可用）
     *
     * @param repairerUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRepairer(@RequestBody RepairerUpdateRequest repairerUpdateRequest) {
        if (repairerUpdateRequest == null || repairerUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Repairer repairer = new Repairer();
        BeanUtils.copyProperties(repairerUpdateRequest, repairer);
        // 数据校验
        repairerService.validRepairer(repairer, false);
        // 判断是否存在
        long id = repairerUpdateRequest.getId();
        Repairer oldRepairer = repairerService.getById(id);
        ThrowUtils.throwIf(oldRepairer == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = repairerService.updateById(repairer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取维修员（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<RepairerVO> getRepairerVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Repairer repairer = repairerService.getById(id);
        ThrowUtils.throwIf(repairer == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(repairerService.getRepairerVO(repairer, request));
    }

    /**
     * 分页获取维修员列表（仅管理员可用）
     *
     * @param repairerQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Repairer>> listRepairerByPage(@RequestBody RepairerQueryRequest repairerQueryRequest) {
        long current = repairerQueryRequest.getCurrent();
        long size = repairerQueryRequest.getPageSize();
        // 查询数据库
        Page<Repairer> repairerPage = repairerService.page(new Page<>(current, size),
                repairerService.getQueryWrapper(repairerQueryRequest));
        return ResultUtils.success(repairerPage);
    }

    /**
     * 分页获取维修员列表（封装类）
     *
     * @param repairerQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<RepairerVO>> listRepairerVOByPage(@RequestBody RepairerQueryRequest repairerQueryRequest,
                                                               HttpServletRequest request) {
        long current = repairerQueryRequest.getCurrent();
        long size = repairerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Repairer> repairerPage = repairerService.page(new Page<>(current, size),
                repairerService.getQueryWrapper(repairerQueryRequest));
        // 获取封装类
        return ResultUtils.success(repairerService.getRepairerVOPage(repairerPage, request));
    }

    /**
     * 分页获取当前登录用户创建的维修员列表
     *
     * @param repairerQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<RepairerVO>> listMyRepairerVOByPage(@RequestBody RepairerQueryRequest repairerQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(repairerQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        repairerQueryRequest.setUserId(loginUser.getId());
        long current = repairerQueryRequest.getCurrent();
        long size = repairerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Repairer> repairerPage = repairerService.page(new Page<>(current, size),
                repairerService.getQueryWrapper(repairerQueryRequest));
        // 获取封装类
        return ResultUtils.success(repairerService.getRepairerVOPage(repairerPage, request));
    }

    /**
     * 编辑维修员（给用户使用）
     *
     * @param repairerEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editRepairer(@RequestBody RepairerEditRequest repairerEditRequest, HttpServletRequest request) {
        if (repairerEditRequest == null || repairerEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Repairer repairer = new Repairer();
        BeanUtils.copyProperties(repairerEditRequest, repairer);
        // 数据校验
        repairerService.validRepairer(repairer, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = repairerEditRequest.getId();
        Repairer oldRepairer = repairerService.getById(id);
        ThrowUtils.throwIf(oldRepairer == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = repairerService.updateById(repairer);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
