package com.scu.Accommodation.model.dto.teacher;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新教师请求
 *
 */
@Data
public class TeacherUpdateRequest implements Serializable {

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

    private String park;

    private String building;

    private String room;

    /**
     *
     */
    private String roomId;

    private Long apartment_id;

    /**
     *
     */
    private String phone;

    private long userId;


    private static final long serialVersionUID = 1L;
}