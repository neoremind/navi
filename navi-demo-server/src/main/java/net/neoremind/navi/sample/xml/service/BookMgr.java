package net.neoremind.navi.sample.xml.service;

import java.util.List;

import net.neoremind.navi.sample.xml.vo.Book;

public interface BookMgr {

	/**
	 * query
	 */
	public Book get(int id);

	public List<Book> getAll();

	/**
	 * add
	 */
	public Book add(Book book);

	/**
	 * delete
	 */
	public void delete(int id);

	/**
	 * update
	 */
	public void update(Book book);
	
}
