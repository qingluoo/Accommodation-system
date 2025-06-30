package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Transaction;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业务视图
 *
 */
@Data
public class TransactionVO implements Serializable {

    private Long id;

    /**
     *
     */
    private String unionId;

    /**
     *
     */
    private String name;

    private Integer grade;


    private String college;


    private String major;

    /**
     *
     */
    private Integer roleId;

    /**
     *
     */
    private String description;


    private String filename;

    /**
     *
     */
    private Integer type;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private String reason;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


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
