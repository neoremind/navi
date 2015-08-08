package com.baidu.beidou.navimgr.util;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangxu
 */
public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    static final ObjectMapper objectMapper;
    private static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {
        if (jsonString != null && !"".equals(jsonString)) {
            try {
                return objectMapper.readValue(jsonString, tr);
            } catch (Exception var3) {
                LOG.warn("json error:" + var3.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }

    public static String toJson(Object object) {
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (Exception var3) {
            LOG.warn("json error:" + var3.getMessage());
        }

        return jsonString;
    }

    public static Object json2Object(String jsonString, Class<?> c) {
        if (jsonString != null && !"".equals(jsonString)) {
            try {
                return objectMapper.readValue(jsonString, c);
            } catch (Exception var3) {
                LOG.warn("json error:" + var3.getMessage());
                return "";
            }
        } else {
            return "";
        }
    }

    static {
        StdSerializerProvider sp = new StdSerializerProvider();
        objectMapper = new ObjectMapper((JsonFactory) null, sp, (DeserializerProvider) null);
    }

    public static final class IosDateTimeJsonSerializer extends JsonSerializer<Date> {
        public IosDateTimeJsonSerializer() {
        }

        public void serialize(Date value, JsonGenerator paramJsonGenerator, SerializerProvider provider) throws
                IOException, JsonProcessingException {
            if (value != null) {
                paramJsonGenerator.writeString(JsonUtils.ISO_DATETIME_FORMAT.format(value));
            }

        }
    }
}

