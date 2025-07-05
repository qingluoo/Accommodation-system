package com.scu.Accommodation.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.scu.Accommodation.utils.ConTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
    public BaseResponse<Long> addTransaction(@ModelAttribute TransactionAddRequest transactionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(transactionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionAddRequest, transaction);
        try {
            if (transactionAddRequest.getFile() != null) {
                transaction.setFilename(transactionAddRequest.getFile().getOriginalFilename());
                byte[] bytes = transactionAddRequest.getFile().getBytes();
                // 现在你可以使用bytes数组了，写入数据库
                transaction.setFilebyte(bytes);
            }
        } catch (IOException e) {
            // 在这里可以记录日志或进行其他异常处理
            ThrowUtils.throwIf(false, ErrorCode.SYSTEM_ERROR, e.getMessage());
        }

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
    public BaseResponse<Boolean> updateTransaction(@RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        if (transactionUpdateRequest == null || transactionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionUpdateRequest, transaction);
        transaction.setUpdateTime(new Date());
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


    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName,@RequestParam("unionId") String unionId,HttpServletRequest request ,HttpServletResponse response) throws Exception {
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("filename", fileName);
        queryWrapper.eq("unionId", unionId);
        Transaction transaction = transactionService.getOne(queryWrapper);
        ThrowUtils.throwIf(transaction == null, ErrorCode.NOT_FOUND_ERROR);
        byte[] bytes = transaction.getFilebyte();
        String ContentType = ConTypeUtil.determineContentType(fileName);
        response.setContentType(ContentType);
        // 处理文件名编码
        String userAgent = request.getHeader("User-Agent");
        String encodedFileName;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            // IE浏览器
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        } else if (userAgent.contains("Mozilla")) {
            // Firefox, Chrome等现代浏览器
            encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } else {
            // 其他浏览器
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        }
        // 设置Content-Disposition头
        String contentDisposition = String.format("attachment; filename=\"%s\"", encodedFileName);
        response.setHeader("Content-Disposition", contentDisposition);

        // 4. 写入文件流（安全写法）
        try (ServletOutputStream os = response.getOutputStream()) {
            os.write(bytes);
            os.flush();
        }
    }

    // endregion
}
