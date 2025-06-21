package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.TeacherMapper;
import com.scu.Accommodation.model.dto.teacher.TeacherQueryRequest;
import com.scu.Accommodation.model.entity.Teacher;
import com.scu.Accommodation.model.vo.TeacherVO;
import com.scu.Accommodation.service.TeacherService;
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
 * 教师服务实现
 *
 */
@Service
@Slf4j
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param teacher
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validTeacher(Teacher teacher, boolean add) {
        ThrowUtils.throwIf(teacher == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = teacher.getUnionId();
        String teaName = teacher.getTeaName();
        Integer sex = teacher.getSex();
        String college = teacher.getCollege();
        String title = teacher.getTitle();
        String phone = teacher.getPhone();

        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR, "unionId不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(teaName), ErrorCode.PARAMS_ERROR, "姓名不能为空");
            ThrowUtils.throwIf(sex == null, ErrorCode.PARAMS_ERROR, "性别不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(college), ErrorCode.PARAMS_ERROR, "学院不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(phone), ErrorCode.PARAMS_ERROR, "手机号不能为空");
        }
    }

    /**
     * 获取查询条件
     *
     * @param teacherQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Teacher> getQueryWrapper(TeacherQueryRequest teacherQueryRequest) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (teacherQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = teacherQueryRequest.getId();
        String unionId = teacherQueryRequest.getUnionId();
        String teaName = teacherQueryRequest.getTeaName();
        Integer sex = teacherQueryRequest.getSex();
        String college = teacherQueryRequest.getCollege();
        String title = teacherQueryRequest.getTitle();
        String roomId = teacherQueryRequest.getRoomId();
        String phone = teacherQueryRequest.getPhone();
        String sortField = teacherQueryRequest.getSortField();
        String sortOrder = teacherQueryRequest.getSortOrder();
        Long userId = teacherQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(roomId), "roomId", roomId);
        queryWrapper.like(StringUtils.isNotBlank(teaName), "teaName", teaName);
        queryWrapper.like(StringUtils.isNotBlank(college), "college", college);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(ObjectUtils.isNotEmpty(sex), "sex", sex);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取教师封装
     *
     * @param teacher
     * @param request
     * @return
     */
    @Override
    public TeacherVO getTeacherVO(Teacher teacher, HttpServletRequest request) {
        // 对象转封装类
        TeacherVO teacherVO = TeacherVO.objToVo(teacher);
        return teacherVO;
    }

    /**
     * 分页获取教师封装
     *
     * @param teacherPage
     * @param request
     * @return
     */
    @Override
    public Page<TeacherVO> getTeacherVOPage(Page<Teacher> teacherPage, HttpServletRequest request) {
        List<Teacher> teacherList = teacherPage.getRecords();
        Page<TeacherVO> teacherVOPage = new Page<>(teacherPage.getCurrent(), teacherPage.getSize(), teacherPage.getTotal());
        if (CollUtil.isEmpty(teacherList)) {
            return teacherVOPage;
        }
        // 对象列表 => 封装对象列表
        List<TeacherVO> teacherVOList = teacherList.stream().map(teacher -> {
            return TeacherVO.objToVo(teacher);
        }).collect(Collectors.toList());

        teacherVOPage.setRecords(teacherVOList);
        return teacherVOPage;
    }

}
