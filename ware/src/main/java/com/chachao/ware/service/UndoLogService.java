package com.chachao.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-07-16 22:29:14
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

