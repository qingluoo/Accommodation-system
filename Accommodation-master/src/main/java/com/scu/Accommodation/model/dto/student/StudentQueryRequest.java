package com.scu.Accommodation.model.dto.student;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询学生请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentQueryRequest extends PageRequest implements Serializable {

    private Long id;
    /**
     *
     */
    private String unionId;

    /**
     *
     */
    private Integer grade;

    /**
     *
     */
    private String stuName;

    /**
     *
     */
    private Integer sex;

    /**
     *
     */
    private String college;

    /**
     *
     */
    private String major;

    /**
     *
     */
    private String classNum;

    /**
     *
     */
    private String roomId;


    private Long userId;


    private static final long serialVersionUID = 1L;
}