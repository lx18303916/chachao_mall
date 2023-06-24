package com.chachao.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chachao.common.valid.AddGroup;
import com.chachao.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.chachao.product.entity.BrandEntity;
import com.chachao.product.service.BrandService;
import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    @PostMapping("/update/status")
    public R updateStatus(@RequestBody Map<String, Object> params) {
        Long brandId = Long.parseLong(params.get("brandId").toString());
        Integer showStatus = Integer.parseInt(params.get("showStatus").toString());

        BrandEntity updateEntity = new BrandEntity();
        updateEntity.setShowStatus(showStatus);

        brandService.update(updateEntity, new UpdateWrapper<BrandEntity>().eq("brand_id", brandId));

        // 返回响应结果
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());

            //TODO 更新其他关联
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
