package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Transaction;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业务视图
 *
 */
@Data
public class TransactionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param transactionVO
     * @return
     */
    public static Transaction voToObj(TransactionVO transactionVO) {
        if (transactionVO == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionVO, transaction);
        return transaction;
    }

    /**
     * 对象转封装类
     *
     * @param transaction
     * @return
     */
    public static TransactionVO objToVo(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionVO transactionVO = new TransactionVO();
        BeanUtils.copyProperties(transaction, transactionVO);
        return transactionVO;
    }
}
