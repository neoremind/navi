package com.baidu.beidou.navi.it;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.it.service.CompanyService;

public class TestProtobufCompanyService extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceDirectCallProtobuf;

    @Test
    public void testGet() {
        testGet(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testGetMulti() {
        testGetMulti(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testGetAll() {
        testGetAll(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testGetMapByIds() {
        testGetMapByIds(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testAdd() {
        testAdd(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testAddNegative() {
        testAddNegative(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testDelete() {
        testDelete(companyServiceDirectCallProtobuf);
    }

    @Test
    public void testInit() {
        testInit(companyServiceDirectCallProtobuf);
    }

}
