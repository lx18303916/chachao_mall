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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.chachao.product.dao.AttrDao;
import com.chachao.product.entity.AttrEntity;
import com.chachao.product.service.AttrService;


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

}