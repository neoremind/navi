package net.neoremind.navi.sample.xml.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.navi.sample.xml.service.BookMgr;
import net.neoremind.navi.sample.xml.vo.Book;

/**
 * Sample xml book service implementing basic CRUD operations
 * 
 * @author Zhang Xu
 */
public class BookMgrImpl implements BookMgr {

	private Map<Integer, Book> bookMap = new HashMap<Integer, Book>();
	private List<Book> bookList = new ArrayList<Book>();

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	{
		try {
			Book book1 = new Book(1, "天方夜谭", 123l, dateFormat.parse("2000-11-11"));
			Book book2 = new Book(999, "读者文摘", 456l, dateFormat.parse("2013-09-30"));
			Book book3 = new Book(12345, "第一财经周刊", 789l, dateFormat.parse("2008-05-30"));

			bookList.add(book1);
			bookList.add(book1);
			bookList.add(book3);

			bookMap.put(book1.getId(), book1);
			bookMap.put(book2.getId(), book2);
			bookMap.put(book3.getId(), book3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * query
	 */
	public Book get(int id) {
		return bookMap.get(id);
	}

	public List<Book> getAll() {
		return bookList;
	}

	/**
	 * add
	 */
	public Book add(Book book) {
		if (book.getId() <= 0) {
			throw new RuntimeException("Book id invliad");
		}
		if (bookMap.containsKey(book.getId())) {
			throw new RuntimeException("Book id deplicate");
		}
		bookMap.put(book.getId(), book);
		bookList.add(book);
		return book;
	}

	/**
	 * delete
	 */
	public void delete(int id) {
		if (!bookMap.containsKey(id)) {
			throw new RuntimeException("Book id not exist");
		}
		bookList.remove(bookMap.get(id));
		bookMap.remove(id);
	}

	/**
	 * update
	 */
	public void update(Book book) {
		if (book.getId() <= 0) {
			throw new RuntimeException("Book id invliad");
		}
		if (!bookMap.containsKey(book.getId())) {
			throw new RuntimeException("Book id not exist");
		}
		bookMap.put(book.getId(), book);
	}

}
