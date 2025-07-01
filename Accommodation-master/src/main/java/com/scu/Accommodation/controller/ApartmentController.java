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
import com.scu.Accommodation.model.dto.apartment.ApartmentAddRequest;
import com.scu.Accommodation.model.dto.apartment.ApartmentEditRequest;
import com.scu.Accommodation.model.dto.apartment.ApartmentQueryRequest;
import com.scu.Accommodation.model.dto.apartment.ApartmentUpdateRequest;
import com.scu.Accommodation.model.entity.Apartment;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.ApartmentVO;
import com.scu.Accommodation.service.ApartmentService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 公寓接口
 *
 */
@RestController
@RequestMapping("/apartment")
@Slf4j
public class ApartmentController {

    @Resource
    private ApartmentService apartmentService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建公寓
     *
     * @param apartmentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addApartment(@RequestBody ApartmentAddRequest apartmentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(apartmentAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Apartment apartment = new Apartment();
        BeanUtils.copyProperties(apartmentAddRequest, apartment);
        // 数据校验
        apartmentService.validApartment(apartment, true);
        // 写入数据库
        boolean result = apartmentService.save(apartment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newApartmentId = apartment.getId();
        return ResultUtils.success(newApartmentId);
    }

    /**
     * 删除公寓
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteApartment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Apartment oldApartment = apartmentService.getById(id);
        ThrowUtils.throwIf(oldApartment == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = apartmentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新公寓（仅管理员可用）
     *
     * @param apartmentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateApartment(@RequestBody ApartmentUpdateRequest apartmentUpdateRequest) {
        if (apartmentUpdateRequest == null || apartmentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Apartment apartment = new Apartment();
        BeanUtils.copyProperties(apartmentUpdateRequest, apartment);
        // 数据校验
        apartmentService.validApartment(apartment, false);
        // 判断是否存在
        long id = apartmentUpdateRequest.getId();
        Apartment oldApartment = apartmentService.getById(id);
        ThrowUtils.throwIf(oldApartment == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = apartmentService.updateById(apartment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取公寓（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ApartmentVO> getApartmentVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Apartment apartment = apartmentService.getById(id);
        ThrowUtils.throwIf(apartment == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(apartmentService.getApartmentVO(apartment, request));
    }

    /**
     * 分页获取公寓列表（仅管理员可用）
     *
     * @param apartmentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Apartment>> listApartmentByPage(@RequestBody ApartmentQueryRequest apartmentQueryRequest) {
        long current = apartmentQueryRequest.getCurrent();
        long size = apartmentQueryRequest.getPageSize();
        // 查询数据库
        Page<Apartment> apartmentPage = apartmentService.page(new Page<>(current, size),
                apartmentService.getQueryWrapper(apartmentQueryRequest));
        return ResultUtils.success(apartmentPage);
    }

    /**
     * 分页获取公寓列表（封装类）
     *
     * @param apartmentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ApartmentVO>> listApartmentVOByPage(@RequestBody ApartmentQueryRequest apartmentQueryRequest,
                                                               HttpServletRequest request) {
        long current = apartmentQueryRequest.getCurrent();
        long size = apartmentQueryRequest.getPageSize();
        // 限制爬虫
        //ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Apartment> apartmentPage = apartmentService.page(new Page<>(current, size),
                apartmentService.getQueryWrapper(apartmentQueryRequest));
        // 获取封装类
        return ResultUtils.success(apartmentService.getApartmentVOPage(apartmentPage, request));
    }

    /**
     * 分页获取当前登录用户创建的公寓列表
     *
     * @param apartmentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ApartmentVO>> listMyApartmentVOByPage(@RequestBody ApartmentQueryRequest apartmentQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(apartmentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        apartmentQueryRequest.setUserId(loginUser.getId());
        long current = apartmentQueryRequest.getCurrent();
        long size = apartmentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Apartment> apartmentPage = apartmentService.page(new Page<>(current, size),
                apartmentService.getQueryWrapper(apartmentQueryRequest));
        // 获取封装类
        return ResultUtils.success(apartmentService.getApartmentVOPage(apartmentPage, request));
    }

    /**
     * 编辑公寓（给用户使用）
     *
     * @param apartmentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editApartment(@RequestBody ApartmentEditRequest apartmentEditRequest, HttpServletRequest request) {
        if (apartmentEditRequest == null || apartmentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Apartment apartment = new Apartment();
        BeanUtils.copyProperties(apartmentEditRequest, apartment);
        // 数据校验
        apartmentService.validApartment(apartment, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = apartmentEditRequest.getId();
        Apartment oldApartment = apartmentService.getById(id);
        ThrowUtils.throwIf(oldApartment == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = apartmentService.updateById(apartment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }



    // endregion
}
