package com.chachao.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.product.entity.AttrEntity;
import com.chachao.product.vo.AttrBaseRequestVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageBase(Map<String, Object> params, Long catId, String type);

    void saveAttr(AttrBaseRequestVo attr);
}

