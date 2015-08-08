package com.baidu.beidou.navi.it.zoo;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestSimpleCompanyServiceWithZoo extends AbstractCompanyServiceZooTest {

    @Resource
    private CompanyService companyServiceEnableZoo;

    @Test
    public void testGet() {
        testGet(companyServiceEnableZoo);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceEnableZoo);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceEnableZoo);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceEnableZoo);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceEnableZoo);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceEnableZoo);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceEnableZoo);
    }

    @Before
    public void testInit() {
        testInit(companyServiceEnableZoo);
    }

}
