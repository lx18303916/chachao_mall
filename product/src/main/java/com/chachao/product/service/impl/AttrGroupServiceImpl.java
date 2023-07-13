package com.chachao.product.service.impl;

import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.Query;
import com.chachao.product.dao.CategoryDao;
import com.chachao.product.entity.AttrEntity;
import com.chachao.product.entity.CategoryEntity;
import com.chachao.product.service.AttrService;
import com.chachao.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private AttrService attrService;
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

    /**
     * 根据分类id 查出所有的分组以及这些组里边的属性
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrByCatelogId(Long catelogId) {

        // 1.查询这个品牌id下所有分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        // 2.查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group ->{
            // 先对拷分组数据
            AttrGroupWithAttrsVo attrVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, attrVo);
            // 按照分组id查询所有关联属性并封装到vo
            List<AttrEntity> attrs = attrService.getRelationAttr(attrVo.getAttrGroupId());
            attrVo.setAttrs(attrs);
            return attrVo;
        }).collect(Collectors.toList());
        return collect;
    }

}