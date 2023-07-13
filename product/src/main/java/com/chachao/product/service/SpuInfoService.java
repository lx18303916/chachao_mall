package com.chachao.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.product.entity.SpuInfoEntity;
import com.chachao.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:23
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);
}

