package com.scu.Accommodation.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.Accommodation.annotation.AuthCheck;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.exception.BusinessException;
import com.scu.Accommodation.mapper.PermissionMapper;
import com.scu.Accommodation.model.entity.Permission;
import com.scu.Accommodation.model.entity.User;
import com.scu.Accommodation.model.enums.UserRoleEnum;
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
    private PermissionMapper permissionMapper;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        String mustCode = authCheck.mustCode();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        // 不需要权限，放行
        if (mustRoleEnum == null || mustCode == null) {
            return joinPoint.proceed();
        }
        checkRole(userRole, mustRole);
        checkPermission(userRole, mustCode);
        // 通过权限校验，放行
        return joinPoint.proceed();
    }

    /**
     * 校验用户角色
     */
    private void checkRole(String userRole, String requiredRole) {
        if (!requiredRole.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "角色权限不足");
        }
    }

    /**
     * 校验用户权限码
     */
    private void checkPermission(String userRole, String requiredPermission) {
        // 查询用户拥有的所有权限码
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleName", userRole)  // 字段名需与数据库一致
                .select("code");  // 只查询 code 列
        List<Permission> permissions = permissionMapper.selectList(queryWrapper);
        List<String> userPermissions = permissions.stream().map(Permission::getCode).collect(Collectors.toList());

        // 检查是否包含所需权限
        if (!userPermissions.contains(requiredPermission)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无操作权限：" + requiredPermission);
        }
    }
}

