package com.baidu.beidou.navi.util;

import org.junit.Test;

public class ZkPathUtilTest {

    @Test
    public void test() {
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a", "/b", "/c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/", "/b", "/c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a", "b", "/c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/", "b", "/c" }));

        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/x/", "/b", "/c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/x", "/b", "c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/x/", "b", "c" }));
        System.out.println(ZkPathUtil.buildPath(new String[] { "/a/x", "b", "/c" }));

    }

}
