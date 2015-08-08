package com.baidu.beidou.sample.xml.service;

import java.util.List;

import com.baidu.beidou.sample.xml.vo.Book;

public interface BookMgr {

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
     * 
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
