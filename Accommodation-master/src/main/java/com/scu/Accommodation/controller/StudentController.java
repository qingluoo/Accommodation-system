package com.scu.Accommodation.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.model.dto.student.StudentAddRequest;
import com.scu.Accommodation.model.dto.student.StudentEditRequest;
import com.scu.Accommodation.model.dto.student.StudentQueryRequest;
import com.scu.Accommodation.model.dto.student.StudentUpdateRequest;
import com.scu.Accommodation.model.entity.Student;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.StudentVO;
import com.scu.Accommodation.service.StudentService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 学生接口
 *
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Resource
    private StudentService studentService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建学生
     *
     * @param studentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addStudent(@RequestBody StudentAddRequest studentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(studentAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Student student = new Student();
        BeanUtils.copyProperties(studentAddRequest, student);
        // 数据校验
        studentService.validStudent(student, true);
        // 写入数据库
        boolean result = studentService.save(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newStudentId = student.getId();
        return ResultUtils.success(newStudentId);
    }

    /**
     * 删除学生
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteStudent(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新学生（仅管理员可用）
     *
     * @param studentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStudent(@RequestBody StudentUpdateRequest studentUpdateRequest) {
        if (studentUpdateRequest == null || studentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Student student = new Student();
        BeanUtils.copyProperties(studentUpdateRequest, student);
        // 数据校验
        studentService.validStudent(student, false);
        // 判断是否存在
        long id = studentUpdateRequest.getId();
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentService.updateById(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取学生（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<StudentVO> getStudentVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Student student = studentService.getById(id);
        ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVO(student, request));
    }

    /**
     * 分页获取学生列表（仅管理员可用）
     *
     * @param studentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Student>> listStudentByPage(@RequestBody StudentQueryRequest studentQueryRequest) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        return ResultUtils.success(studentPage);
    }

    /**
     * 分页获取学生列表（封装类）
     *
     * @param studentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<StudentVO>> listStudentVOByPage(@RequestBody StudentQueryRequest studentQueryRequest,
                                                               HttpServletRequest request) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVOPage(studentPage, request));
    }

    /**
     * 分页获取当前登录用户创建的学生列表
     *
     * @param studentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<StudentVO>> listMyStudentVOByPage(@RequestBody StudentQueryRequest studentQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(studentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        studentQueryRequest.setUserId(loginUser.getId());
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size),
                studentService.getQueryWrapper(studentQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVOPage(studentPage, request));
    }

    /**
     * 编辑学生（给用户使用）
     *
     * @param studentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editStudent(@RequestBody StudentEditRequest studentEditRequest, HttpServletRequest request) {
        if (studentEditRequest == null || studentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Student student = new Student();
        BeanUtils.copyProperties(studentEditRequest, student);
        // 数据校验
        studentService.validStudent(student, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = studentEditRequest.getId();
        Student oldStudent = studentService.getById(id);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentService.updateById(student);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
