package com.baidu.beidou.navi.it;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestJsonCompanyService extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceDirectCallJson;

    @Test
    public void testGet() {
        testGet(companyServiceDirectCallJson);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceDirectCallJson);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceDirectCallJson);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceDirectCallJson);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceDirectCallJson);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceDirectCallJson);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceDirectCallJson);
    }

    @Before
    public void testInit() {
        testInit(companyServiceDirectCallJson);
    }

}
