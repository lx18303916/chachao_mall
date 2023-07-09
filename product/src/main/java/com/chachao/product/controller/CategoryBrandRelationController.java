package com.chachao.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chachao.product.entity.BrandEntity;
import com.chachao.product.entity.CategoryEntity;
import com.chachao.product.service.BrandService;
import com.chachao.product.service.CategoryService;
import com.chachao.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chachao.product.entity.CategoryBrandRelationEntity;
import com.chachao.product.service.CategoryBrandRelationService;
import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    @RequestMapping("/catelog/list")
    public R getRelation(@RequestParam Map<String, Object> params){
        String brandId = params.get("brandId").toString();
        List<CategoryBrandRelationEntity> relation = categoryBrandRelationService
                .list(new QueryWrapper<CategoryBrandRelationEntity>()
                        .eq("brand_id", Long.parseLong(brandId)));
        return R.ok().put("data", relation);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        CategoryEntity categoryEntity = categoryService.getById(categoryBrandRelation.getCatelogId());
        BrandEntity brandEntity = brandService.getById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 获取分类下的所有品牌id和名字
     */
    @RequestMapping("/brands/list")
    public R brandList(@RequestParam Long catId){
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);

        List<Object> collect = vos.stream().map(eachBrand -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(eachBrand.getBrandId());
            brandVo.setBrandName(eachBrand.getName());
            return brandVo;
        }).collect(Collectors.toList());

        return R.ok().put("data", collect);
    }

}
