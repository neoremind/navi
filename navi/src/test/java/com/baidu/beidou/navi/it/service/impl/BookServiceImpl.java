package com.baidu.beidou.navi.it.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.beidou.navi.it.service.BookService;
import com.baidu.beidou.navi.vo.Book;
import com.baidu.beidou.navi.vo.BookBuilder;

/**
 * ClassName: BookServiceImpl <br/>
 * Function: 测试用书籍实现，用XML暴露服务
 * 
 * @author Zhang Xu
 */
public class BookServiceImpl implements BookService {

    private Map<Integer, Book> bookMap = new HashMap<Integer, Book>();

    private Map<String, Book> bookNameMap = new HashMap<String, Book>();

    private List<Book> bookList = new ArrayList<Book>();

    {
        init();
    }

    /**
     * init
     */
    public void init() {
        bookList = BookBuilder.buildMulti();
        for (Book book : bookList) {
            bookMap.put(book.getId(), book);
            bookNameMap.put(book.getName(), book);
        }
    }

    /**
     * query
     */
    public Book get(int id) {
        System.out.println("this is get(int)");
        return bookMap.get(id);
    }

    /**
     * query
     */
    public Book get(String name) {
        System.out.println("this is get(String)");
        return bookNameMap.get(name);
    }

    /**
     * query
     */
    public Book get(int id, String name) {
        System.out.println("this is get(id, name)");
        if (bookMap.containsKey(id) && bookNameMap.containsKey(name)) {
            return bookMap.get(id);
        }
        return null;
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
