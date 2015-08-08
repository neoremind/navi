package com.baidu.beidou.navi.it;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestHACompanyService2 extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceHANatureOrder;

    @Test
    public void testGet() {
        testGet(companyServiceHANatureOrder);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceHANatureOrder);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceHANatureOrder);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceHANatureOrder);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceHANatureOrder);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceHANatureOrder);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceHANatureOrder);
    }

    @Before
    public void testInit() {
        testInit(companyServiceHANatureOrder);
    }

}
