package com.chachao.product.dao;

import com.chachao.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
