package com.baidu.beidou.navi.it;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestSimpleCompanyService extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceDirectCall;

    @Test
    public void testGet() {
        testGet(companyServiceDirectCall);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceDirectCall);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceDirectCall);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceDirectCall);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceDirectCall);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceDirectCall);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceDirectCall);
    }

    @Test
    public void testInit() {
        testInit(companyServiceDirectCall);
    }

}
