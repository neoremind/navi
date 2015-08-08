package com.baidu.beidou.sample.xml.vo;

import java.util.Date;

public class Book {

	// 书籍ID
	private int id;

	// 书籍URL
	private String name;

	// 书籍页数
	private long pageNum;

	// 书籍发布时间
	private Date publishDate;

	public Book(int id, String name, long pageNum, Date publishDate) {
		super();
		this.id = id;
		this.name = name;
		this.pageNum = pageNum;
		this.publishDate = publishDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPageNum() {
		return pageNum;
	}

	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}
