package com.chachao.product.service.impl;

import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.chachao.product.dao.AttrAttrgroupRelationDao;
import com.chachao.product.entity.AttrAttrgroupRelationEntity;
import com.chachao.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteBatch(AttrAttrgroupRelationEntity[] attrAttrgroupRelation) {
        for(int i = 0; i < attrAttrgroupRelation.length; i++) {
            attrAttrgroupRelationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrAttrgroupRelation[i].getAttrId()).eq("attr_group_id", attrAttrgroupRelation[i].getAttrGroupId()));
        }
    }

}