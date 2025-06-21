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
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

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
