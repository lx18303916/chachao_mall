package com.chachao.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:23
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

