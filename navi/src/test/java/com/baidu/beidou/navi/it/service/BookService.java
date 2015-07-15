package com.baidu.beidou.navi.it.service;

import java.util.List;

import com.baidu.beidou.navi.vo.Book;

/**
 * ClassName: BookMgr <br/>
 * Function: 测试用书籍服务
 * 
 * @author Zhang Xu
 */
public interface BookService {

    /**
     * init
     */
    void init();

    /**
     * query
     */
    Book get(int id);

    /**
     * query
     */
    Book get(String name);

    /**
     * query
     */
    Book get(int id, String name);

    /**
     * get all
     */
    List<Book> getAll();

    /**
     * add
     */
    Book add(Book book);

    /**
     * delete
     */
    void delete(int id);

    /**
     * update
     */
    void update(Book book);

}
