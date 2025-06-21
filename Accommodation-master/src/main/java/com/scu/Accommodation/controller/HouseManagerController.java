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
import com.scu.Accommodation.model.dto.housemanager.HouseManagerAddRequest;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerEditRequest;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerQueryRequest;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerUpdateRequest;
import com.scu.Accommodation.model.entity.HouseManager;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.HouseManagerVO;
import com.scu.Accommodation.service.HouseManagerService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 宿舍总管接口
 *
 */
@RestController
@RequestMapping("/housemanager")
@Slf4j
public class HouseManagerController {

    @Resource
    private HouseManagerService housemanagerService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建宿舍总管
     *
     * @param housemanagerAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addHouseManager(@RequestBody HouseManagerAddRequest housemanagerAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(housemanagerAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        HouseManager housemanager = new HouseManager();
        BeanUtils.copyProperties(housemanagerAddRequest, housemanager);
        // 数据校验
        housemanagerService.validHouseManager(housemanager, true);
        // 写入数据库
        boolean result = housemanagerService.save(housemanager);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newHouseManagerId = housemanager.getId();
        return ResultUtils.success(newHouseManagerId);
    }

    /**
     * 删除宿舍总管
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteHouseManager(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        HouseManager oldHouseManager = housemanagerService.getById(id);
        ThrowUtils.throwIf(oldHouseManager == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = housemanagerService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新宿舍总管（仅管理员可用）
     *
     * @param housemanagerUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateHouseManager(@RequestBody HouseManagerUpdateRequest housemanagerUpdateRequest) {
        if (housemanagerUpdateRequest == null || housemanagerUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        HouseManager housemanager = new HouseManager();
        BeanUtils.copyProperties(housemanagerUpdateRequest, housemanager);
        // 数据校验
        housemanagerService.validHouseManager(housemanager, false);
        // 判断是否存在
        long id = housemanagerUpdateRequest.getId();
        HouseManager oldHouseManager = housemanagerService.getById(id);
        ThrowUtils.throwIf(oldHouseManager == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = housemanagerService.updateById(housemanager);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取宿舍总管（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<HouseManagerVO> getHouseManagerVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        HouseManager housemanager = housemanagerService.getById(id);
        ThrowUtils.throwIf(housemanager == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(housemanagerService.getHouseManagerVO(housemanager, request));
    }

    /**
     * 分页获取宿舍总管列表（仅管理员可用）
     *
     * @param housemanagerQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<HouseManager>> listHouseManagerByPage(@RequestBody HouseManagerQueryRequest housemanagerQueryRequest) {
        long current = housemanagerQueryRequest.getCurrent();
        long size = housemanagerQueryRequest.getPageSize();
        // 查询数据库
        Page<HouseManager> housemanagerPage = housemanagerService.page(new Page<>(current, size),
                housemanagerService.getQueryWrapper(housemanagerQueryRequest));
        return ResultUtils.success(housemanagerPage);
    }

    /**
     * 分页获取宿舍总管列表（封装类）
     *
     * @param housemanagerQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<HouseManagerVO>> listHouseManagerVOByPage(@RequestBody HouseManagerQueryRequest housemanagerQueryRequest,
                                                               HttpServletRequest request) {
        long current = housemanagerQueryRequest.getCurrent();
        long size = housemanagerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<HouseManager> housemanagerPage = housemanagerService.page(new Page<>(current, size),
                housemanagerService.getQueryWrapper(housemanagerQueryRequest));
        // 获取封装类
        return ResultUtils.success(housemanagerService.getHouseManagerVOPage(housemanagerPage, request));
    }

    /**
     * 分页获取当前登录用户创建的宿舍总管列表
     *
     * @param housemanagerQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<HouseManagerVO>> listMyHouseManagerVOByPage(@RequestBody HouseManagerQueryRequest housemanagerQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(housemanagerQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        housemanagerQueryRequest.setUserId(loginUser.getId());
        long current = housemanagerQueryRequest.getCurrent();
        long size = housemanagerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<HouseManager> housemanagerPage = housemanagerService.page(new Page<>(current, size),
                housemanagerService.getQueryWrapper(housemanagerQueryRequest));
        // 获取封装类
        return ResultUtils.success(housemanagerService.getHouseManagerVOPage(housemanagerPage, request));
    }

    /**
     * 编辑宿舍总管（给用户使用）
     *
     * @param housemanagerEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editHouseManager(@RequestBody HouseManagerEditRequest housemanagerEditRequest, HttpServletRequest request) {
        if (housemanagerEditRequest == null || housemanagerEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        HouseManager housemanager = new HouseManager();
        BeanUtils.copyProperties(housemanagerEditRequest, housemanager);
        // 数据校验
        housemanagerService.validHouseManager(housemanager, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = housemanagerEditRequest.getId();
        HouseManager oldHouseManager = housemanagerService.getById(id);
        ThrowUtils.throwIf(oldHouseManager == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = housemanagerService.updateById(housemanager);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
