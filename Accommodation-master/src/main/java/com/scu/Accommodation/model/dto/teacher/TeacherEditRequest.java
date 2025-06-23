package com.scu.Accommodation.model.dto.teacher;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑教师请求
 *
 */
@Data
public class TeacherEditRequest implements Serializable {

    private Long id;
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

    private Long userId;

    private static final long serialVersionUID = 1L;
}