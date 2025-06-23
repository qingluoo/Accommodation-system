package com.scu.Accommodation.model.dto.teacher;

import com.scu.Accommodation.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询教师请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeacherQueryRequest extends PageRequest implements Serializable {

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