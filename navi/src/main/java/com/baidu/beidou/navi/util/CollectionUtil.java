package com.baidu.beidou.navi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: CollectionUtil <br/>
 * Function: 有关集合处理的工具类，通过静态方法消除泛型编译警告。
 * 
 * @author Zhang Xu
 */
public class CollectionUtil {

    /**
     * 判断<code>Map</code>是否为<code>null</code>或空<code>{}</code>
     * 
     * @param map
     *            ## @see Map
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || (map.size() == 0);
    }

    /**
     * 判断Map是否不为<code>null</code>和空<code>{}</code>
     * 
     * @param map
     *            ## @see Map
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return (map != null) && (map.size() > 0);
    }

    /**
     * 判断<code>Collection</code>是否为<code>null</code>或空数组<code>[]</code>。
     * 
     * @param collection
     * @see Collection
     * @return 如果为空, 则返回<code>true</code>
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || (collection.size() == 0);
    }

    /**
     * 判断Collection是否不为<code>null</code>和空数组<code>[]</code>。
     * 
     * @param collection
     * @return 如果不为空, 则返回<code>true</code>
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && (collection.size() > 0);
    }

    /**
     * 创建<code>ArrayList</code>实例
     * 
     * @param <E>
     * @return <code>ArrayList</code>实例
     */
    public static <E> ArrayList<E> createArrayList() {
        return new ArrayList<E>();
    }

    /**
     * 创建<code>ArrayList</code>实例
     * 
     * @param <E>
     * @param initialCapacity
     *            初始化容量
     * @return <code>ArrayList</code>实例
     */
    public static <E> ArrayList<E> createArrayList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    /**
     * 创建<code>HashMap</code>实例
     * 
     * @param <K>
     * @param <V>
     * @return <code>HashMap</code>实例
     */
    public static <K, V> HashMap<K, V> createHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * 创建<code>HashMap</code>实例
     * 
     * @param <K>
     * @param <V>
     * @param initialCapacity
     *            初始化容量
     * @return <code>HashMap</code>实例
     */
    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }

    /**
     * 创建<code>HashMap</code>实例
     * 
     * @param <K>
     * @param <V>
     * @param initialCapacity
     *            初始化容量
     * @param loadFactor
     *            加载因子
     * @return <code>HashMap</code>实例
     */
    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity, float loadFactor) {
        return new HashMap<K, V>(initialCapacity, loadFactor);
    }

    /**
     * List转换
     * 
     * @param fromList
     * @param function
     * @return
     */
    public static <F, T> List<T> transform(Collection<F> fromList,
            Function<? super F, ? extends T> function) {
        if (CollectionUtil.isEmpty(fromList)) {
            return Collections.emptyList();
        }

        List<T> ret = new ArrayList<T>(fromList.size());
        for (F f : fromList) {
            T t = function.apply(f);
            if (t == null) {
                continue;
            }
            ret.add(t);
        }

        return ret;
    }

}
