package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.TransactionMapper;
import com.scu.Accommodation.model.dto.transaction.TransactionQueryRequest;
import com.scu.Accommodation.model.entity.Transaction;
import com.scu.Accommodation.model.vo.TransactionVO;
import com.scu.Accommodation.service.TransactionService;
import com.scu.Accommodation.service.UserService;
import com.scu.Accommodation.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务服务实现
 *
 */
@Service
@Slf4j
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param transaction
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validTransaction(Transaction transaction, boolean add) {
        ThrowUtils.throwIf(transaction == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = transaction.getUnionId();
        String name = transaction.getName();
        Integer roleId = transaction.getRoleId();
        Integer type = transaction.getType();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(roleId == null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(type == null, ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param transactionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Transaction> getQueryWrapper(TransactionQueryRequest transactionQueryRequest) {
        QueryWrapper<Transaction> queryWrapper = new QueryWrapper<>();
        if (transactionQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String unionId = transactionQueryRequest.getUnionId();
        String name = transactionQueryRequest.getName();
        Integer grade = transactionQueryRequest.getGrade();
        String college = transactionQueryRequest.getCollege();
        String major = transactionQueryRequest.getMajor();
        Integer roleId = transactionQueryRequest.getRoleId();
        String description = transactionQueryRequest.getDescription();
        Integer type = transactionQueryRequest.getType();
        Integer status = transactionQueryRequest.getStatus();
        // todo 补充需要的查询条件
        // 排序规则
        String sortField = transactionQueryRequest.getSortField();
        String sortOrder = transactionQueryRequest.getSortOrder();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(college), "college", college);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        // 精确查询
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(major), "major", major);
        queryWrapper.eq(ObjectUtils.isNotEmpty(grade), "grade", grade);
        queryWrapper.eq(ObjectUtils.isNotEmpty(roleId), "roleId", roleId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(type), "type", type);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取业务封装
     *
     * @param transaction
     * @param request
     * @return
     */
    @Override
    public TransactionVO getTransactionVO(Transaction transaction, HttpServletRequest request) {
        // 对象转封装类
        TransactionVO transactionVO = TransactionVO.objToVo(transaction);


        return transactionVO;
    }

    /**
     * 分页获取业务封装
     *
     * @param transactionPage
     * @param request
     * @return
     */
    @Override
    public Page<TransactionVO> getTransactionVOPage(Page<Transaction> transactionPage, HttpServletRequest request) {
        List<Transaction> transactionList = transactionPage.getRecords();
        Page<TransactionVO> transactionVOPage = new Page<>(transactionPage.getCurrent(), transactionPage.getSize(), transactionPage.getTotal());
        if (CollUtil.isEmpty(transactionList)) {
            return transactionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<TransactionVO> transactionVOList = transactionList.stream().map(transaction -> {
            return TransactionVO.objToVo(transaction);
        }).collect(Collectors.toList());

        transactionVOPage.setRecords(transactionVOList);
        return transactionVOPage;
    }

}
