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
import com.scu.Accommodation.model.dto.transaction.TransactionAddRequest;
import com.scu.Accommodation.model.dto.transaction.TransactionEditRequest;
import com.scu.Accommodation.model.dto.transaction.TransactionQueryRequest;
import com.scu.Accommodation.model.dto.transaction.TransactionUpdateRequest;
import com.scu.Accommodation.model.entity.Transaction;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.TransactionVO;
import com.scu.Accommodation.service.TransactionService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 业务接口
 *
 */
@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {

    @Resource
    private TransactionService transactionService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建业务
     *
     * @param transactionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTransaction(@RequestBody TransactionAddRequest transactionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(transactionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionAddRequest, transaction);
        // 数据校验
        transactionService.validTransaction(transaction, true);
        // 写入数据库
        boolean result = transactionService.save(transaction);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newTransactionId = transaction.getId();
        return ResultUtils.success(newTransactionId);
    }

    /**
     * 删除业务
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTransaction(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Transaction oldTransaction = transactionService.getById(id);
        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = transactionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新业务（仅管理员可用）
     *
     * @param transactionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTransaction(@RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        if (transactionUpdateRequest == null || transactionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionUpdateRequest, transaction);
        // 数据校验
        transactionService.validTransaction(transaction, false);
        // 判断是否存在
        long id = transactionUpdateRequest.getId();
        Transaction oldTransaction = transactionService.getById(id);
        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = transactionService.updateById(transaction);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取业务（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TransactionVO> getTransactionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Transaction transaction = transactionService.getById(id);
        ThrowUtils.throwIf(transaction == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(transactionService.getTransactionVO(transaction, request));
    }

    /**
     * 分页获取业务列表（仅管理员可用）
     *
     * @param transactionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Transaction>> listTransactionByPage(@RequestBody TransactionQueryRequest transactionQueryRequest) {
        long current = transactionQueryRequest.getCurrent();
        long size = transactionQueryRequest.getPageSize();
        // 查询数据库
        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size),
                transactionService.getQueryWrapper(transactionQueryRequest));
        return ResultUtils.success(transactionPage);
    }

    /**
     * 分页获取业务列表（封装类）
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TransactionVO>> listTransactionVOByPage(@RequestBody TransactionQueryRequest transactionQueryRequest,
                                                               HttpServletRequest request) {
        long current = transactionQueryRequest.getCurrent();
        long size = transactionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size),
                transactionService.getQueryWrapper(transactionQueryRequest));
        // 获取封装类
        return ResultUtils.success(transactionService.getTransactionVOPage(transactionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的业务列表
     *
     * @param transactionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TransactionVO>> listMyTransactionVOByPage(@RequestBody TransactionQueryRequest transactionQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(transactionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        transactionQueryRequest.setUserId(loginUser.getId());
        long current = transactionQueryRequest.getCurrent();
        long size = transactionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Transaction> transactionPage = transactionService.page(new Page<>(current, size),
                transactionService.getQueryWrapper(transactionQueryRequest));
        // 获取封装类
        return ResultUtils.success(transactionService.getTransactionVOPage(transactionPage, request));
    }

    /**
     * 编辑业务（给用户使用）
     *
     * @param transactionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTransaction(@RequestBody TransactionEditRequest transactionEditRequest, HttpServletRequest request) {
        if (transactionEditRequest == null || transactionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionEditRequest, transaction);
        // 数据校验
        transactionService.validTransaction(transaction, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = transactionEditRequest.getId();
        Transaction oldTransaction = transactionService.getById(id);
        ThrowUtils.throwIf(oldTransaction == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = transactionService.updateById(transaction);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
