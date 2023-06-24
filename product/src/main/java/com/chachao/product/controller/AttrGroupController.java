package com.chachao.product.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.chachao.product.entity.CategoryEntity;
import com.chachao.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chachao.product.entity.AttrGroupEntity;
import com.chachao.product.service.AttrGroupService;
import com.chachao.common.utils.PageUtils;
import com.chachao.common.utils.R;



/**
 * 属性分组
 *
 * @author chenhao
 * @email 18303916@qq.com
 * @date 2023-06-21 10:28:24
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params, 0L);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catID}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catID") Long catId){
        PageUtils page = attrGroupService.queryPage(params, catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        CategoryEntity categoryEntity = categoryService.getById(attrGroup.getCatelogId());
        List<Long> path = new ArrayList<>();
        attrGroupService.findCatIdPath(categoryEntity, path);
        Collections.reverse(path);
        Long[] arrpath = new Long[path.size()];
        for(int i = 0; i < path.size(); i++) arrpath[i] = path.get(i);
        attrGroup.setCatelogPath(arrpath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
