package com.chachao.product;

import com.chachao.product.dao.AttrGroupDao;
import com.chachao.product.entity.AttrGroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductApplicationTests {
    @Autowired
    AttrGroupDao attrGroupDao;
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(100);
        System.out.printf(attrGroupEntity.toString());
    }

}
