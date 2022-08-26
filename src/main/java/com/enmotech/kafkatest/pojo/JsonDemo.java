package com.enmotech.kafkatest.pojo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * com.enmotech.kafkatest.pojo
 *
 * @author syf
 * @create 2022-08-23-14:07
 * @Description kafka-test
 */

public class JsonDemo {
    //注解的作用：控制json转换字符串时属性的顺序
    @JSONField(ordinal = 2)
    private payload payload;
    @JSONField(ordinal = 1)
    private schema schema;

    public JsonDemo(schema schema, payload payload) {
        this.schema = schema;
        this.payload = payload;
    }

    public schema getSchema() {
        return schema;
    }

    public void setSchema(schema schema) {
        this.schema = schema;
    }

    public payload getPayload() {
        return payload;
    }

    public void setPayload(payload payload) {
        this.payload = payload;
    }
}
