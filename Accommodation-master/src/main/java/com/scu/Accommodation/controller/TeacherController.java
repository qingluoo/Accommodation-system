package com.scu.Accommodation.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.model.dto.teacher.TeacherAddRequest;
import com.scu.Accommodation.model.dto.teacher.TeacherEditRequest;
import com.scu.Accommodation.model.dto.teacher.TeacherQueryRequest;
import com.scu.Accommodation.model.dto.teacher.TeacherUpdateRequest;
import com.scu.Accommodation.model.entity.Student;
import com.scu.Accommodation.model.entity.Teacher;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.TeacherVO;
import com.scu.Accommodation.service.TeacherService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * 教师接口
 *
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建教师
     *
     * @param teacherAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeacher(@RequestBody TeacherAddRequest teacherAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(teacherAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherAddRequest, teacher);
        // 数据校验
        teacherService.validTeacher(teacher, true);
        // 写入数据库
        boolean result = teacherService.save(teacher);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newTeacherId = teacher.getId();
        return ResultUtils.success(newTeacherId);
    }

    /**
     * 删除教师
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeacher(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Teacher oldTeacher = teacherService.getById(id);
        ThrowUtils.throwIf(oldTeacher == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = teacherService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新教师（仅管理员可用）
     *
     * @param teacherUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeacher(@RequestBody TeacherUpdateRequest teacherUpdateRequest) {
        if (teacherUpdateRequest == null || teacherUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherUpdateRequest, teacher);
        // 数据校验
        teacherService.validTeacher(teacher, false);
        // 判断是否存在
        long id = teacherUpdateRequest.getId();
        Teacher oldTeacher = teacherService.getById(id);
        ThrowUtils.throwIf(oldTeacher == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = teacherService.updateById(teacher);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 根据 id 获取教师（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TeacherVO> getTeacherVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Teacher teacher = teacherService.getById(id);
        ThrowUtils.throwIf(teacher == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(teacherService.getTeacherVO(teacher, request));
    }

    /**
     * 分页获取教师列表（仅管理员可用）
     *
     * @param teacherQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Teacher>> listTeacherByPage(@RequestBody TeacherQueryRequest teacherQueryRequest) {
        long current = teacherQueryRequest.getCurrent();
        long size = teacherQueryRequest.getPageSize();
        // 查询数据库
        Page<Teacher> teacherPage = teacherService.page(new Page<>(current, size),
                teacherService.getQueryWrapper(teacherQueryRequest));
        return ResultUtils.success(teacherPage);
    }

    /**
     * 分页获取教师列表（封装类）
     *
     * @param teacherQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TeacherVO>> listTeacherVOByPage(@RequestBody TeacherQueryRequest teacherQueryRequest,
                                                               HttpServletRequest request) {
        long current = teacherQueryRequest.getCurrent();
        long size = teacherQueryRequest.getPageSize();
        // 限制爬虫
        //ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Teacher> teacherPage = teacherService.page(new Page<>(current, size),
                teacherService.getQueryWrapper(teacherQueryRequest));
        // 获取封装类
        return ResultUtils.success(teacherService.getTeacherVOPage(teacherPage, request));
    }

    /**
     * 分页获取当前登录用户创建的教师列表
     *
     * @param teacherQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TeacherVO>> listMyTeacherVOByPage(@RequestBody TeacherQueryRequest teacherQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(teacherQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        teacherQueryRequest.setUserId(loginUser.getId());
        long current = teacherQueryRequest.getCurrent();
        long size = teacherQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Teacher> teacherPage = teacherService.page(new Page<>(current, size),
                teacherService.getQueryWrapper(teacherQueryRequest));
        // 获取封装类
        return ResultUtils.success(teacherService.getTeacherVOPage(teacherPage, request));
    }

    /**
     * 编辑教师（给用户使用）
     *
     * @param teacherEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTeacher(@RequestBody TeacherEditRequest teacherEditRequest, HttpServletRequest request) {
        if (teacherEditRequest == null || teacherEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherEditRequest, teacher);
        // 数据校验
        teacherService.validTeacher(teacher, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = teacherEditRequest.getId();
        Teacher oldTeacher = teacherService.getById(id);
        ThrowUtils.throwIf(oldTeacher == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = teacherService.updateById(teacher);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    // 批量导入
    @PostMapping("/import")
    public BaseResponse<Boolean> importData(MultipartFile file) throws Exception {
        //拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //通过Reader读取excel里面的数据
        reader.addHeaderAlias("工号", "unionId");
        reader.addHeaderAlias("姓名", "teaName");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("学院", "college");
        reader.addHeaderAlias("职称", "title");
        reader.addHeaderAlias("电话", "phone");
        List<Teacher> teacherList = reader.readAll(Teacher.class);
        //将数据写入数据库
        for (Teacher teacher : teacherList) {
            teacherService.save(teacher);
        }
        return ResultUtils.success(true);
    }

}
