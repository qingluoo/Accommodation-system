package com.scu.Accommodation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.Accommodation.model.dto.transaction.TransactionQueryRequest;
import com.scu.Accommodation.model.entity.Transaction;
import com.scu.Accommodation.model.vo.TransactionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务服务
 *
 */
public interface TransactionService extends IService<Transaction> {

    /**
     * 校验数据
     *
     * @param transaction
     * @param add 对创建的数据进行校验
     */
    void validTransaction(Transaction transaction, boolean add);

    /**
     * 获取查询条件
     *
     * @param transactionQueryRequest
     * @return
     */
    QueryWrapper<Transaction> getQueryWrapper(TransactionQueryRequest transactionQueryRequest);
    
    /**
     * 获取业务封装
     *
     * @param transaction
     * @param request
     * @return
     */
    TransactionVO getTransactionVO(Transaction transaction, HttpServletRequest request);

    /**
     * 分页获取业务封装
     *
     * @param transactionPage
     * @param request
     * @return
     */
    Page<TransactionVO> getTransactionVOPage(Page<Transaction> transactionPage, HttpServletRequest request);
}
