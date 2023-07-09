package com.chachao.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chachao.common.utils.PageUtils;
import com.chachao.member.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-07-08 11:30:37
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

