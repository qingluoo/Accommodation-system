package com.scu.Accommodation.model.dto.transaction;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 创建业务请求
 *
 */
@Data
public class TransactionAddRequest implements Serializable {

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


    private MultipartFile file;

    /**
     *
     */
    private Integer type;


    private Long userId;


    private static final long serialVersionUID = 1L;
}