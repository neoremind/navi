package com.baidu.beidou.navi.vo;

import java.util.ArrayList;
import java.util.List;

public class BookBuilder {

    public static List<Book> buildMulti() {
        List<Book> ret = new ArrayList<Book>();

        Book book1 = new Book(1, "天方夜谭");
        Book book2 = new Book(999, "读者文摘");
        Book book3 = new Book(12345, "第一财经周刊");

        ret.add(book1);
        ret.add(book2);
        ret.add(book3);

        return ret;
    }

}
