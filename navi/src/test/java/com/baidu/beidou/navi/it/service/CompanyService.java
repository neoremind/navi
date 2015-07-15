package com.baidu.beidou.navi.it.service;

import java.util.List;
import java.util.Map;

import com.baidu.beidou.navi.vo.Company;

/**
 * ClassName: CompanyMgr <br/>
 * Function: 测试用服务，一个公司举例的服务
 * 
 * @author Zhang Xu
 */
public interface CompanyService {

    /**
     * init
     */
    void init();

    /**
     * 根据公司id获取公司
     * 
     * @param id
     * @return
     */
    Company get(int id);

    /**
     * 批量的{@link #get(int)}
     * 
     * @param ids
     * @return
     */
    List<Company> getByIds(List<Integer> ids);

    /**
     * 根据公司id批量获取id到公司的字典
     * 
     * @param ids
     * @return
     */
    Map<Integer, Company> getMapByIds(List<Integer> ids);

    /**
     * 获取所有
     * 
     * @return
     */
    List<Company> getAll();

    /**
     * 添加
     * 
     * @param company
     * @return
     */
    Company add(Company company);

    /**
     * 删除
     * 
     * @param id
     */
    void delete(int id);

    /**
     * 更新
     * 
     * @param company
     */
    void update(Company company);

}
