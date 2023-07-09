package com.chachao.product.service.impl;

import com.chachao.common.constant.ProductConstant;
import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.Query;
import com.chachao.product.dao.AttrAttrgroupRelationDao;
import com.chachao.product.dao.AttrGroupDao;
import com.chachao.product.entity.AttrAttrgroupRelationEntity;
import com.chachao.product.entity.AttrGroupEntity;
import com.chachao.product.entity.CategoryEntity;
import com.chachao.product.service.AttrAttrgroupRelationService;
import com.chachao.product.service.CategoryService;
import com.chachao.product.vo.AttrBaseRequestVo;
import com.chachao.product.vo.AttrBaseRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.chachao.product.dao.AttrDao;
import com.chachao.product.entity.AttrEntity;
import com.chachao.product.service.AttrService;
import org.springframework.util.CollectionUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    AttrAttrgroupRelationDao attrRelationDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageBase(Map<String, Object> params, Long catId, String type) {
        IPage<AttrEntity> page = null;
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<>();
        attrEntityQueryWrapper.eq("attr_type",
                "base".equalsIgnoreCase(type)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catId == 0) {
            page = this.page(
                    new Query<AttrEntity>().getPage(params),
                    attrEntityQueryWrapper
            );
        } else {
            page = this.page(
                    new Query<AttrEntity>().getPage(params),
                    attrEntityQueryWrapper.eq("catelog_id", catId)
            );
        }
        List<AttrBaseRespVo> attrBaseRespVoList = page.getRecords().stream().map(attrEntity -> {
           AttrBaseRespVo attrBaseRespVo = new AttrBaseRespVo();
           BeanUtils.copyProperties(attrEntity, attrBaseRespVo);
            CategoryEntity byId = categoryService.getById(attrEntity.getCatelogId());
            attrBaseRespVo.setCatelogName(byId.getName());


            if("base".equalsIgnoreCase(type)) {
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrRelationDao
                        .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                    attrBaseRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            return attrBaseRespVo;

        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrBaseRespVoList);

        return pageUtils;
    }

    @Override
    public void saveAttr(AttrBaseRequestVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId()!=null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities
                = attrRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relationEntities.stream().map(relationEntitie -> {
            return relationEntitie.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities = new ArrayList<>();
        if(!CollectionUtils.isEmpty(attrIds)){
            attrEntities = attrDao.selectBatchIds(attrIds);
        }

        return attrEntities;
    }

    @Override
    public PageUtils getAttrNoRelation(Map<String, Object> params, Long attrGroupId) {

        //找出当前分组所属分类，查出分类关联的分组
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        List<AttrGroupEntity> attrGroups = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", attrGroupEntity.getCatelogId()));
        List<Long> attrGroupIds = attrGroups.stream().map(eachAttrGroup -> {
            return eachAttrGroup.getAttrGroupId();
        }).collect(Collectors.toList());
        //找出分组关联的属性
        List<AttrAttrgroupRelationEntity> relationEntities = attrRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> relateAttrIds = relationEntities.stream().map(eachRelationEntity -> {
            return eachRelationEntity.getAttrId();
        }).collect(Collectors.toList());
        //排除关系表中这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", attrGroupEntity.getCatelogId()).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if(relateAttrIds != null && relateAttrIds.size() >0) {
            wrapper.notIn("attr_id", relateAttrIds);
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

}