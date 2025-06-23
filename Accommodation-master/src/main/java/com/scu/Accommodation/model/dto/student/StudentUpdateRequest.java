package com.scu.Accommodation.model.dto.student;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新学生请求
 *
 */
@Data
public class StudentUpdateRequest implements Serializable {

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