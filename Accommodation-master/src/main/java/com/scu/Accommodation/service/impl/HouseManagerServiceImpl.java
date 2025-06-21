package com.scu.Accommodation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.Accommodation.common.ErrorCode;
import com.scu.Accommodation.constant.CommonConstant;
import com.scu.Accommodation.exception.ThrowUtils;
import com.scu.Accommodation.mapper.HouseManagerMapper;
import com.scu.Accommodation.model.dto.housemanager.HouseManagerQueryRequest;
import com.scu.Accommodation.model.entity.HouseManager;
import com.scu.Accommodation.model.vo.HouseManagerVO;
import com.scu.Accommodation.service.HouseManagerService;
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
 * 宿舍总管服务实现
 *
 */
@Service
@Slf4j
public class HouseManagerServiceImpl extends ServiceImpl<HouseManagerMapper, HouseManager> implements HouseManagerService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param housemanager
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validHouseManager(HouseManager housemanager, boolean add) {
        ThrowUtils.throwIf(housemanager == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String unionId = housemanager.getUnionId();
        String name = housemanager.getName();
        String phone = housemanager.getPhone();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(unionId), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(phone), ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询条件
     *
     * @param housemanagerQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<HouseManager> getQueryWrapper(HouseManagerQueryRequest housemanagerQueryRequest) {
        QueryWrapper<HouseManager> queryWrapper = new QueryWrapper<>();
        if (housemanagerQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = housemanagerQueryRequest.getId();
        Long notId = housemanagerQueryRequest.getNotId();
        String title = housemanagerQueryRequest.getTitle();
        String content = housemanagerQueryRequest.getContent();
        String searchText = housemanagerQueryRequest.getSearchText();
        String sortField = housemanagerQueryRequest.getSortField();
        String sortOrder = housemanagerQueryRequest.getSortOrder();
        List<String> tagList = housemanagerQueryRequest.getTags();
        Long userId = housemanagerQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取宿舍总管封装
     *
     * @param housemanager
     * @param request
     * @return
     */
    @Override
    public HouseManagerVO getHouseManagerVO(HouseManager housemanager, HttpServletRequest request) {
        // 对象转封装类
        HouseManagerVO housemanagerVO = HouseManagerVO.objToVo(housemanager);

        return housemanagerVO;
    }

    /**
     * 分页获取宿舍总管封装
     *
     * @param housemanagerPage
     * @param request
     * @return
     */
    @Override
    public Page<HouseManagerVO> getHouseManagerVOPage(Page<HouseManager> housemanagerPage, HttpServletRequest request) {
        List<HouseManager> housemanagerList = housemanagerPage.getRecords();
        Page<HouseManagerVO> housemanagerVOPage = new Page<>(housemanagerPage.getCurrent(), housemanagerPage.getSize(), housemanagerPage.getTotal());
        if (CollUtil.isEmpty(housemanagerList)) {
            return housemanagerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<HouseManagerVO> housemanagerVOList = housemanagerList.stream().map(housemanager -> {
            return HouseManagerVO.objToVo(housemanager);
        }).collect(Collectors.toList());


        housemanagerVOPage.setRecords(housemanagerVOList);
        return housemanagerVOPage;
    }

}
