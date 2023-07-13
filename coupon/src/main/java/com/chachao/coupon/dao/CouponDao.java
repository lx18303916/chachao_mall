package com.chachao.coupon.dao;

import com.chachao.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-07-12 20:49:25
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
