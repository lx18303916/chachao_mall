package com.chachao.common.to;

import lombok.Data;

/**
 * <p>Title: SkuHasStockVo</p>
 * Description：存储这个sku是否有库存
 */
@Data
public class SkuHasStockVo {

	private Long skuId;

	/**是否有库存*/
	private Boolean hasStock;
}
