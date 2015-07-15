package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.navi.it.exception.IdDuplicateException;
import com.baidu.beidou.navi.it.service.CompanyService;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.vo.Company;
import com.baidu.beidou.navi.vo.CompanyBuilder;

public class AbstractCompanyServiceTest extends BaseTest {

    public void testGet(CompanyService companyService) {
        Company c = companyService.get(88);
        validateCompany(c);
    }

    public void testGetMulti(CompanyService companyService) {
        List<Integer> ids = CollectionUtil.createArrayList();
        ids.add(88);
        ids.add(99);
        ids.add(100);
        List<Company> c = companyService.getByIds(ids);
        assertThat(c.size(), is(2));
        validateCompany(c.get(0));
    }

    public void testGetAll(CompanyService companyService) {
        List<Company> c = companyService.getAll();
        assertThat(c.size(), is(2));
        validateCompany(c.get(0));
    }

    public void testGetMapByIds(CompanyService companyService) {
        List<Integer> ids = CollectionUtil.createArrayList();
        ids.add(88);
        ids.add(99);
        ids.add(100);
        Map<Integer, Company> c = companyService.getMapByIds(ids);
        assertThat(c.size(), is(3));
        validateCompany(c.get(88));
    }

    public void testAdd(CompanyService companyService) {
        Company c = CompanyBuilder.buildSimple();
        c.setId(555);
        Company ret = companyService.add(c);
        validateCompany(ret, 555);

        Company ret2 = companyService.get(555);
        validateCompany(ret2, 555);
    }

    public void testAddNegative(CompanyService companyService) {
        Company c = CompanyBuilder.buildSimple();
        try {
            c.setId(555);
            companyService.add(c);
            companyService.add(c);
        } catch (IdDuplicateException e) {
            assertThat(e.getClass().getName(), is(IdDuplicateException.class.getName()));
            return;
        } catch (Throwable t) {
            assertThat(t.getCause().getMessage(), is("Company id duplicate"));
            return;
        }
        fail("should not get here");
    }

    public void testInit(CompanyService companyService) {
        companyService.init();
    }

    public void testDelete(CompanyService companyService) {
        companyService.delete(99);
        List<Company> c = companyService.getAll();
        assertThat(c.size(), is(2));
        validateCompany(c.get(0));
    }

    private void validateCompany(Company c) {
        validateCompany(c, 88);
    }

    private void validateCompany(Company c, int id) {
        assertThat(c.getId(), is(id));
        assertThat(c.getName(), is("百度时代网络技术(北京)有限公司"));
        assertThat(c.getDepartmentList().size(), is(4));
        assertThat(c.getDepartmentList().get(0).getId(), is(101));
        assertThat(c.getDepartmentList().get(0).getName(), is("R&D"));
    }

}
