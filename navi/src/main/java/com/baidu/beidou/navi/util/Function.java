package com.baidu.beidou.navi.util;

/**
 * ClassName: Function <br/>
 * Function: 配合{@link ListUtil}使用的function回调
 * 
 * @author Zhang Xu
 */
public interface Function<F, T> {

    /**
     * Returns the result of applying this function to {@code input}. This method is <i>generally expected</i>, but not
     * absolutely required, to have the following properties:
     * <ul>
     * <li>Its execution does not cause any observable side effects.
     * <li>The computation is <i>consistent with equals</i>; that is, {@link Objects#equal Objects.equal}{@code (a, b)}
     * implies that {@code Objects.equal(function.apply(a), function.apply(b))}.
     * </ul>
     * 
     * @throws NullPointerException
     *             if {@code input} is null and this function does not accept null arguments
     */
    T apply(F input);

}
