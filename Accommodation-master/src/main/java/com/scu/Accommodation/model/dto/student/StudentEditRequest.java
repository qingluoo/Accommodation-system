package com.scu.Accommodation.model.dto.student;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑学生请求
 *
 */
@Data
public class StudentEditRequest implements Serializable {

    private long id;
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

    private long userId;


    private static final long serialVersionUID = 1L;
}