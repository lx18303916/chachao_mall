package com.chachao.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> findTree(List<CategoryEntity> all, List<CategoryEntity> parentNodes);

    void updateDetail(CategoryEntity category);
}

