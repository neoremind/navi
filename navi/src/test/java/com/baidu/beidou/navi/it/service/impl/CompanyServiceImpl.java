package com.baidu.beidou.navi.it.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.baidu.beidou.navi.it.exception.IdDuplicateException;
import com.baidu.beidou.navi.it.service.CompanyService;
import com.baidu.beidou.navi.server.annotation.NaviRpcService;
import com.baidu.beidou.navi.vo.Company;
import com.baidu.beidou.navi.vo.CompanyBuilder;

/**
 * ClassName: CompanyServiceImpl <br/>
 * Function: 测试用服务，一个公司举例的服务，用注解方式暴露服务
 * 
 * @author Zhang Xu
 */
@Service
@NaviRpcService(serviceInterface = CompanyService.class)
public class CompanyServiceImpl implements CompanyService {

    private Map<Integer, Company> companyMap = new HashMap<Integer, Company>();

    private List<Company> companyList = new ArrayList<Company>();

    @PostConstruct
    @Override
    public void init() {
        try {
            companyList.clear();
            companyList = CompanyBuilder.buildMulti();
            companyMap.clear();
            for (Company c : companyList) {
                companyMap.put(c.getId(), c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Company get(int id) {
        return companyMap.get(id);
    }

    @Override
    public List<Company> getByIds(List<Integer> ids) {
        List<Company> result = new ArrayList<Company>();
        for (Integer id : ids) {
            if (companyMap.containsKey(id)) {
                result.add(companyMap.get(id));
            }
        }

        return result;
    }

    @Override
    public Map<Integer, Company> getMapByIds(List<Integer> ids) {
        Map<Integer, Company> result = new HashMap<Integer, Company>();
        for (Integer id : ids) {
            result.put(id, companyMap.get(id));
        }
        return result;
    }

    @Override
    public List<Company> getAll() {
        return companyList;
    }

    @Override
    public Company add(Company company) {
        if (company.getId() <= 0 || company.getId() <= 0) {
            throw new RuntimeException("Company id invliad");
        }
        if (companyMap.containsKey(company.getId())) {
            throw new IdDuplicateException("Company id duplicate");
        }
        companyMap.put(company.getId(), company);
        companyList.add(company);
        return company;
    }

    @Override
    public void delete(int id) {
        if (!companyMap.containsKey(id)) {
            throw new RuntimeException("Company id not exist");
        }
        companyList.remove(companyMap.get(id));
        companyMap.remove(id);
    }

    @Override
    public void update(Company company) {
        if (company.getId() <= 0 || company.getId() <= 0) {
            throw new RuntimeException("Company id invliad");
        }
        if (!companyMap.containsKey(company.getId())) {
            throw new RuntimeException("Company id not exist");
        }
        companyMap.put(company.getId(), company);
    }

}
