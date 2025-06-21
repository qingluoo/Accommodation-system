package com.scu.Accommodation.model.dto.teacher;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建教师请求
 *
 */
@Data
public class TeacherAddRequest implements Serializable {

    private long id;
    /**
     *
     */
    private String unionId;

    /**
     *
     */
    private String teaName;

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
    private String title;

    /**
     *
     */
    private String roomId;

    /**
     *
     */
    private String phone;


    private long userId;


    private static final long serialVersionUID = 1L;
}