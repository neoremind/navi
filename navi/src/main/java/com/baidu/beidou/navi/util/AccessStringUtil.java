package com.baidu.beidou.navi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.codec.json.JsonMapper;

/**
 * ClassName: AccessStringUtil <br/>
 * Function: 记录日志的工具类，做截断用
 * 
 * @author Zhang Xu
 */
public class AccessStringUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AccessStringUtil.class);

    private static final JsonMapper jsonMapper = JsonMapper.buildNormalMapper();

    private static final int TO_STRING_MAX_LENGTH = 800;

    public static String toJson(Object object) {
        try {
            return cutByMaxLength(jsonMapper.toJson(object));
        } catch (Exception e) {
            LOG.error("Failed to serialize to json - " + object);
            return StringUtil.EMPTY;
        }
    }

    private static String cutByMaxLength(String str) {
        if (StringUtil.isEmpty(str)) {
            return StringUtil.EMPTY;
        } else {
            return str.length() > TO_STRING_MAX_LENGTH ? str.substring(0, TO_STRING_MAX_LENGTH)
                    : str;
        }
    }

}
