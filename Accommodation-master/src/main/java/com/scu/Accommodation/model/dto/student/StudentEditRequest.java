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

    private Long id;
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

    private String park;

    private String building;

    private String room;

    /**
     *
     */
    private String roomId;

    private Long apartment_id;

    private Long userId;


    private static final long serialVersionUID = 1L;
}