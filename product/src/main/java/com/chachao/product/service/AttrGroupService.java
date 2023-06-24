package com.chachao.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.product.entity.AttrGroupEntity;
import com.chachao.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params, Long catId);

    void findCatIdPath(CategoryEntity categoryEntity, List<Long> path);
}

