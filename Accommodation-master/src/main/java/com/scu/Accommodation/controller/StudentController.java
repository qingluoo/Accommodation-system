package com.scu.Accommodation.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.BaseResponse;
import com.scu.Accommodation.common.DeleteRequest;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.common.ResultUtils;
import com.scu.Accommodation.constant.UserConstant;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.StudentMapper;
import com.scu.Accommodation.model.dto.apartment.ApartmentUpdateRequest;
import com.scu.Accommodation.model.dto.student.StudentAddRequest;
import com.scu.Accommodation.model.dto.student.StudentEditRequest;
import com.scu.Accommodation.model.dto.student.StudentQueryRequest;
import com.scu.Accommodation.model.dto.student.StudentUpdateRequest;
import com.scu.Accommodation.model.entity.Apartment;
import com.scu.Accommodation.model.entity.Student;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.vo.StudentVO;
import com.scu.Accommodation.service.ApartmentService;
import com.scu.Accommodation.service.StudentService;
import com.scu.Accommodation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

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
    private StudentMapper studentMapper;

    @Resource
    private UserService userService;

    @Resource
    private ApartmentService apartmentService;

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
     * 更新学生（仅管理员可用）
     *
     * @param studentUpdateRequest
     * @return
     */
    @PostMapping("/updateByUnionId")
    public BaseResponse<Boolean> updateStudentByUnionId(@RequestBody StudentUpdateRequest studentUpdateRequest) {
        if (studentUpdateRequest == null || studentUpdateRequest.getUnionId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Student student = new Student();
        BeanUtils.copyProperties(studentUpdateRequest, student);
        // 数据校验
        studentService.validStudent(student, false);
        // 判断是否存在
        String unionId = studentUpdateRequest.getUnionId();
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("unionId", unionId);
        Student oldStudent = studentService.getOne(queryWrapper);
        ThrowUtils.throwIf(oldStudent == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentService.update(student, queryWrapper);
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

    @GetMapping("/get/vo/byUnionId")
    public BaseResponse<StudentVO> getStudentVOByUnionId(@RequestParam String unionId, HttpServletRequest request) {
        ThrowUtils.throwIf(unionId == null, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("unionId", unionId);
        Student student = studentService.getOne(queryWrapper);
        ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(studentService.getStudentVO(student, request));
    }

    @PostMapping("/list/page/stuWithNoRoom")
    public BaseResponse<Page<Student>> listStudentWithNoRoom(@RequestBody StudentQueryRequest studentQueryRequest) {
        long current = studentQueryRequest.getCurrent();
        long size = studentQueryRequest.getPageSize();
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("park", studentQueryRequest.getPark());
        queryWrapper.eq("building", studentQueryRequest.getBuilding());
        queryWrapper.isNull("room");
        // 查询数据库
        Page<Student> studentPage = studentService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(studentPage);
    }

    /**
     * 分页获取学生列表（仅管理员可用）
     *
     * @param studentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
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
        //ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
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

    // 批量导入学生
    @PostMapping("/import")
    public BaseResponse<Boolean> importData(MultipartFile file) throws Exception {
        //拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //通过Reader读取excel里面的数据
        reader.addHeaderAlias("学号", "unionId");
        reader.addHeaderAlias("年级", "grade");
        reader.addHeaderAlias("姓名", "stuName");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("学院", "college");
        reader.addHeaderAlias("专业", "major");
        reader.addHeaderAlias("班级", "classNum");
        List<Student> studentList=reader.readAll(Student.class);
        //将数据写入数据库
        for(Student student:studentList){
            studentService.save(student);
        }
        return ResultUtils.success(true);
    }

    /**
     * 退宿舍
     */
    @PostMapping("/quitroom")
    public BaseResponse<Boolean> quitRoom(@RequestParam String unionId, HttpServletRequest request) {
        if (unionId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("unionId", unionId);
        Student student = studentService.getOne(queryWrapper);
        ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = studentMapper.quitRoom(unionId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 批量退宿
     */
    @PostMapping("/quitroom/batch")
    public BaseResponse<Boolean> quitRoomBatch(@RequestBody List<String> unionIds, HttpServletRequest request) {
        if (unionIds == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        for(String unionId:unionIds){
            QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unionId", unionId);
            Student student = studentService.getOne(queryWrapper);
            ThrowUtils.throwIf(student == null, ErrorCode.NOT_FOUND_ERROR);
            boolean result = studentMapper.quitRoom(unionId);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 批量更新学生寝室信息
     */
    @PostMapping("/update/park-building")
    public BaseResponse<Boolean> updateParkBuilding(@RequestParam MultipartFile file,@RequestParam String[] selectedParkBuildingList) throws Exception {
        //拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //通过Reader读取excel里面的数据
        reader.addHeaderAlias("学号", "unionId");
        reader.addHeaderAlias("年级", "grade");
        reader.addHeaderAlias("姓名", "stuName");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("学院", "college");
        reader.addHeaderAlias("专业", "major");
        reader.addHeaderAlias("班级", "classNum");
        List<Student> students=reader.readAll(Student.class);
        //分类学生(按性别分类男女学生)
        List<Student> maleStudents = new ArrayList<>();
        List<Student> femaleStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getSex() == 1) {
                maleStudents.add(student);
            } else if (student.getSex() == 0) {
                femaleStudents.add(student);
            }
        }
        //将男女学生按专业排序
        maleStudents.sort(Comparator.comparing(Student::getMajor));
        femaleStudents.sort(Comparator.comparing(Student::getMajor));

        //获取每个索引对应的楼栋可住的女生人数
        Map<Integer, Integer> canLiveFemaleNumMap = new HashMap<>();
        //获取每个索引对应的楼栋可住的男生人数
        Map<Integer, Integer> canLiveMaleNumMap = new HashMap<>();
        int index = 0;
        for (int i = 0; i < selectedParkBuildingList.length; i++) {
            QueryWrapper<Apartment> queryWrapper = new QueryWrapper<>();
            //查询该楼栋的女生可住房间
            queryWrapper.eq("park", selectedParkBuildingList[i].split("-")[0]);
            queryWrapper.eq("building", selectedParkBuildingList[i].split("-")[1]);
            queryWrapper.like("roomType", "女");
            List<Apartment> femaleRooms = apartmentService.list(queryWrapper);
            //当前楼栋可住的女生人数 = 女生可住房间的总床位 - 女生可住房间的已住人数
            int femaleBeds = femaleRooms.stream().mapToInt(Apartment::getBedNum).sum();
            int femaleLived = femaleRooms.stream().mapToInt(Apartment::getLiveNum).sum();
            canLiveFemaleNumMap.put(index, femaleBeds - femaleLived);
            //查询该楼栋的男生可住房间
            QueryWrapper<Apartment> maleWrapper = new QueryWrapper<>();
            maleWrapper.eq("park",selectedParkBuildingList[i].split("-")[0]);
            maleWrapper.eq("building", selectedParkBuildingList[i].split("-")[1]);
            maleWrapper.like("roomType", "男");
            List<Apartment> maleRooms = apartmentService.list(maleWrapper);
            //当前楼栋可住的男生人数 = 男生可住房间的总床位 - 男生可住房间的已住人数
            int maleBeds = maleRooms.stream().mapToInt(Apartment::getBedNum).sum();
            int maleLived = maleRooms.stream().mapToInt(Apartment::getLiveNum).sum();
            canLiveMaleNumMap.put(index, maleBeds - maleLived);
            index++;
        }

        // 分配女生到楼栋
        assignToBuildingOnly(femaleStudents, selectedParkBuildingList, "女", canLiveFemaleNumMap);
        // 分配男生到楼栋
        assignToBuildingOnly(maleStudents, selectedParkBuildingList, "男", canLiveMaleNumMap);

        return ResultUtils.success(true);
    }

    public void assignToBuildingOnly(List<Student> students,
                                     String[] selectedParkBuildingList,
                                     String genderType,
                                     Map<Integer, Integer> canLiveMap) {
        int stuIndex = 0; // 当前处理的学生索引
        int buildingIndex = 0; // 当前处理的楼栋索引

        while (stuIndex < students.size() && buildingIndex < selectedParkBuildingList.length) {
            int canAssignNum = canLiveMap.getOrDefault(buildingIndex, 0);
            if (canAssignNum <= 0) {
                buildingIndex++;
                continue;
            }

            // 拆分园区-楼栋字符串
            String[] parts = selectedParkBuildingList[buildingIndex].split("-");
            String park = parts[0];
            String building = parts[1];

            // 本轮可分配的人数
            int assignCount = Math.min(canAssignNum, students.size() - stuIndex);

            for (int i = 0; i < assignCount; i++) {
                Student stu = students.get(stuIndex++);
                stu.setPark(park);
                stu.setBuilding(building);

                // 更新数据库记录（建议确认 Student 的主键是否为 unionId）
                QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("unionId", stu.getUnionId());
                studentService.update(stu, queryWrapper);
            }

            buildingIndex++;
        }

        if (stuIndex < students.size()) {
            System.err.println("⚠️ [" + genderType + "生] 还有 " + (students.size() - stuIndex) + " 人未能分配宿舍楼！");
        }
    }
}
