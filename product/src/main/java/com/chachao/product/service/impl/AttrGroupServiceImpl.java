package com.chachao.product.service.impl;

import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.Query;
import com.chachao.product.dao.CategoryDao;
import com.chachao.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chachao.product.dao.AttrGroupDao;
import com.chachao.product.entity.AttrGroupEntity;
import com.chachao.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        IPage<AttrGroupEntity> page = null;
        if(catId == 0) {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>()
            );
        } else {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId)
            );
        }


        return new PageUtils(page);
    }

    @Override
    public void findCatIdPath(CategoryEntity categoryEntity, List<Long> path) {
        path.add(categoryEntity.getCatId());
        if(categoryEntity.getParentCid() != 0) {
            CategoryEntity categoryEntity1 = categoryDao.selectById(categoryEntity.getParentCid());
            findCatIdPath(categoryEntity1, path);
        }
        return;
    }

}