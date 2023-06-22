package com.chachao.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.Query;

import com.chachao.product.dao.CategoryDao;
import com.chachao.product.entity.CategoryEntity;
import com.chachao.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> findTree(List<CategoryEntity> all, List<CategoryEntity> parentNodes) {
        //遍历每一个把children填上
        for(CategoryEntity each : parentNodes) {
            List<CategoryEntity> curChildrenNode = all.stream()
                    .filter(entity -> entity.getParentCid().equals(each.getCatId()))
                    .sorted((entity1, entity2)->{return (entity1.getSort() == null?0 : entity1.getSort()) - (entity2.getSort() == null? 0 : entity2.getSort());})
                    .collect(Collectors.toList());

            each.setChildren(curChildrenNode);
            findTree(all, curChildrenNode);
        }

        return parentNodes;
    }

}