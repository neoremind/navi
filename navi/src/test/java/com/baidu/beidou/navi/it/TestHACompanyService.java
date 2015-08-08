package com.baidu.beidou.navi.it;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestHACompanyService extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceHA;

    @Test
    public void testGet() {
        testGet(companyServiceHA);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceHA);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceHA);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceHA);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceHA);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceHA);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceHA);
    }
    
    @Before
    public void testInit() {
        testInit(companyServiceHA);
    }

}
