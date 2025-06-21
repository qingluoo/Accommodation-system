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
import com.scu.Accommodation.model.dto.houseparents.HouseparentsAddRequest;
import com.scu.Accommodation.model.dto.houseparents.HouseparentsEditRequest;
import com.scu.Accommodation.model.dto.houseparents.HouseparentsQueryRequest;
import com.scu.Accommodation.model.dto.houseparents.HouseparentsUpdateRequest;
import com.scu.Accommodation.model.entity.Houseparents;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.HouseparentsVO;
import com.scu.Accommodation.service.HouseparentsService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 宿管接口
 *
 */
@RestController
@RequestMapping("/houseparents")
@Slf4j
public class HouseparentsController {

    @Resource
    private HouseparentsService houseparentsService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建宿管
     *
     * @param houseparentsAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addHouseparents(@RequestBody HouseparentsAddRequest houseparentsAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(houseparentsAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Houseparents houseparents = new Houseparents();
        BeanUtils.copyProperties(houseparentsAddRequest, houseparents);
        // 数据校验
        houseparentsService.validHouseparents(houseparents, true);
        // 写入数据库
        boolean result = houseparentsService.save(houseparents);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newHouseparentsId = houseparents.getId();
        return ResultUtils.success(newHouseparentsId);
    }

    /**
     * 删除宿管
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteHouseparents(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Houseparents oldHouseparents = houseparentsService.getById(id);
        ThrowUtils.throwIf(oldHouseparents == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        // 操作数据库
        boolean result = houseparentsService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新宿管（仅管理员可用）
     *
     * @param houseparentsUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateHouseparents(@RequestBody HouseparentsUpdateRequest houseparentsUpdateRequest) {
        if (houseparentsUpdateRequest == null || houseparentsUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Houseparents houseparents = new Houseparents();
        BeanUtils.copyProperties(houseparentsUpdateRequest, houseparents);
        // 数据校验
        houseparentsService.validHouseparents(houseparents, false);
        // 判断是否存在
        long id = houseparentsUpdateRequest.getId();
        Houseparents oldHouseparents = houseparentsService.getById(id);
        ThrowUtils.throwIf(oldHouseparents == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = houseparentsService.updateById(houseparents);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取宿管（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<HouseparentsVO> getHouseparentsVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Houseparents houseparents = houseparentsService.getById(id);
        ThrowUtils.throwIf(houseparents == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(houseparentsService.getHouseparentsVO(houseparents, request));
    }

    /**
     * 分页获取宿管列表（仅管理员可用）
     *
     * @param houseparentsQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Houseparents>> listHouseparentsByPage(@RequestBody HouseparentsQueryRequest houseparentsQueryRequest) {
        long current = houseparentsQueryRequest.getCurrent();
        long size = houseparentsQueryRequest.getPageSize();
        // 查询数据库
        Page<Houseparents> houseparentsPage = houseparentsService.page(new Page<>(current, size),
                houseparentsService.getQueryWrapper(houseparentsQueryRequest));
        return ResultUtils.success(houseparentsPage);
    }

    /**
     * 分页获取宿管列表（封装类）
     *
     * @param houseparentsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<HouseparentsVO>> listHouseparentsVOByPage(@RequestBody HouseparentsQueryRequest houseparentsQueryRequest,
                                                               HttpServletRequest request) {
        long current = houseparentsQueryRequest.getCurrent();
        long size = houseparentsQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Houseparents> houseparentsPage = houseparentsService.page(new Page<>(current, size),
                houseparentsService.getQueryWrapper(houseparentsQueryRequest));
        // 获取封装类
        return ResultUtils.success(houseparentsService.getHouseparentsVOPage(houseparentsPage, request));
    }

    /**
     * 分页获取当前登录用户创建的宿管列表
     *
     * @param houseparentsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<HouseparentsVO>> listMyHouseparentsVOByPage(@RequestBody HouseparentsQueryRequest houseparentsQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(houseparentsQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        houseparentsQueryRequest.setUserId(loginUser.getId());
        long current = houseparentsQueryRequest.getCurrent();
        long size = houseparentsQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Houseparents> houseparentsPage = houseparentsService.page(new Page<>(current, size),
                houseparentsService.getQueryWrapper(houseparentsQueryRequest));
        // 获取封装类
        return ResultUtils.success(houseparentsService.getHouseparentsVOPage(houseparentsPage, request));
    }

    /**
     * 编辑宿管（给用户使用）
     *
     * @param houseparentsEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editHouseparents(@RequestBody HouseparentsEditRequest houseparentsEditRequest, HttpServletRequest request) {
        if (houseparentsEditRequest == null || houseparentsEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Houseparents houseparents = new Houseparents();
        BeanUtils.copyProperties(houseparentsEditRequest, houseparents);
        // 数据校验
        houseparentsService.validHouseparents(houseparents, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = houseparentsEditRequest.getId();
        Houseparents oldHouseparents = houseparentsService.getById(id);
        ThrowUtils.throwIf(oldHouseparents == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = houseparentsService.updateById(houseparents);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
