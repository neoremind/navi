package com.baidu.beidou.navi.util;

/**
 * ClassName: ArrayUtil <br/>
 * Function: 有关数组处理的工具类
 * 
 * @author Zhang Xu
 */
public class ArrayUtil {

    /**
     * 检查数组是否为<code>null</code>或空数组<code>[]</code>。
     * <p/>
     * 
     * <pre>
     * ArrayUtil.isEmpty(null)              = true
     * ArrayUtil.isEmpty(new int[0])        = true
     * ArrayUtil.isEmpty(new int[10])       = false
     * </pre>
     * 
     * @param array
     *            要检查的数组
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

}
