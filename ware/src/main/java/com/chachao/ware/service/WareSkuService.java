package com.chachao.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.to.SkuHasStockVo;
import com.chachao.common.utils.PageUtils;
import com.chachao.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-07-16 22:29:13
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

