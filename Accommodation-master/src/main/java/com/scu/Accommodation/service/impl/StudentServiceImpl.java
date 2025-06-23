package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.StudentMapper;
import com.scu.Accommodation.model.dto.student.StudentQueryRequest;
import com.scu.Accommodation.model.entity.Student;
import com.scu.Accommodation.model.vo.StudentVO;
import com.scu.Accommodation.service.StudentService;
import com.scu.Accommodation.service.UserService;
import com.scu.Accommodation.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生服务实现
 *
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param student
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validStudent(Student student, boolean add) {
        ThrowUtils.throwIf(student == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = student.getUnionId();
        Integer grade = student.getGrade();
        String stuName = student.getStuName();
        Integer sex = student.getSex();
        String college = student.getCollege();
        String major = student.getMajor();
        String classNum = student.getClassNum();
        String roomId = student.getRoomId();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR, "unionId不能为空");
            ThrowUtils.throwIf(grade == null, ErrorCode.PARAMS_ERROR, "年级不为空");
            ThrowUtils.throwIf(StringUtils.isBlank(stuName), ErrorCode.PARAMS_ERROR, "姓名不能为空");
            ThrowUtils.throwIf(sex == null || sex < 0 || sex > 2, ErrorCode.PARAMS_ERROR, "性别错误");
            ThrowUtils.throwIf(StringUtils.isBlank(college), ErrorCode.PARAMS_ERROR, "学院不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(major), ErrorCode.PARAMS_ERROR, "专业不能为空");
        }
    }

    /**
     * 获取查询条件
     *
     * @param studentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Student> getQueryWrapper(StudentQueryRequest studentQueryRequest) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (studentQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        String unionId = studentQueryRequest.getUnionId();
        Integer grade = studentQueryRequest.getGrade();
        String stuName = studentQueryRequest.getStuName();
        Integer sex = studentQueryRequest.getSex();
        String college = studentQueryRequest.getCollege();
        String major = studentQueryRequest.getMajor();
        String classNum = studentQueryRequest.getClassNum();
        String roomId = studentQueryRequest.getRoomId();
        String sortField = studentQueryRequest.getSortField();
        String sortOrder = studentQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(ObjectUtils.isNotEmpty(stuName), "stuName", stuName);
        queryWrapper.like(ObjectUtils.isNotEmpty(roomId), "roomId", roomId);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(unionId), "unionId", unionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(college), "college", college);
        queryWrapper.eq(ObjectUtils.isNotEmpty(major), "major", major);
        queryWrapper.eq(ObjectUtils.isNotEmpty(grade), "grade", grade);
        queryWrapper.eq(ObjectUtils.isNotEmpty(classNum), "classNum", classNum);
        queryWrapper.eq(ObjectUtils.isNotEmpty(sex), "sex", sex);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取学生封装
     *
     * @param student
     * @param request
     * @return
     */
    @Override
    public StudentVO getStudentVO(Student student, HttpServletRequest request) {
        // 对象转封装类
        StudentVO studentVO = StudentVO.objToVo(student);
        return studentVO;
    }

    /**
     * 分页获取学生封装
     *
     * @param studentPage
     * @param request
     * @return
     */
    @Override
    public Page<StudentVO> getStudentVOPage(Page<Student> studentPage, HttpServletRequest request) {
        List<Student> studentList = studentPage.getRecords();
        Page<StudentVO> studentVOPage = new Page<>(studentPage.getCurrent(), studentPage.getSize(), studentPage.getTotal());
        if (CollUtil.isEmpty(studentList)) {
            return studentVOPage;
        }
        // 对象列表 => 封装对象列表
        List<StudentVO> studentVOList = studentList.stream().map(student -> {
            return StudentVO.objToVo(student);
        }).collect(Collectors.toList());

        studentVOPage.setRecords(studentVOList);
        return studentVOPage;
    }

}
