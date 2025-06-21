package com.scu.Accommodation.model.vo;

import cn.hutool.json.JSONUtil;
import com.scu.Accommodation.model.entity.Teacher;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师视图
 *
 */
@Data
public class TeacherVO implements Serializable {

    /**
     * id
     */
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


    /**
     * 封装类转对象
     *
     * @param teacherVO
     * @return
     */
    public static Teacher voToObj(TeacherVO teacherVO) {
        if (teacherVO == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherVO, teacher);
        return teacher;
    }

    /**
     * 对象转封装类
     *
     * @param teacher
     * @return
     */
    public static TeacherVO objToVo(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        TeacherVO teacherVO = new TeacherVO();
        BeanUtils.copyProperties(teacher, teacherVO);
        return teacherVO;
    }
}
