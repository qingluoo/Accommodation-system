package com.scu.Accommodation.aop;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.mapper.PermissionMapper;
import com.scu.Accommodation.mapper.RoleMapper;
import com.scu.Accommodation.model.entity.Permission;
import com.scu.Accommodation.model.entity.Role;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.enums.UserRoleEnum;
import com.scu.Accommodation.service.RoleService;
import com.scu.Accommodation.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限校验 AOP
 *
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustCode = authCheck.mustCode();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();

        // 不需要权限，放行
        if (mustCode == null) {
            return joinPoint.proceed();
        }
        checkCode(userRole, mustCode);
        // 通过权限校验，放行
        return joinPoint.proceed();
    }

    /**
     * 校验用户权限码
     */
    private void checkCode(String userRole, String requiredCode) {
        // 查询用户拥有的所有权限码
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userRole)  // 字段名需与数据库一致
                .select("codelist");  // 只查询 code 列
        Role role = roleService.getOne(queryWrapper);
        // 解析 codelist 字符串为 JSON 数组
        if (role != null && role.getCodelist() != null) {
            List<String> userPermissions = JSONUtil.toList(role.getCodelist(), String.class);
            // 检查是否包含所需权限
            if (!userPermissions.contains(requiredCode)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无操作权限：" + requiredCode);
            }
        } else {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "角色权限不足");
        }
    }
}

