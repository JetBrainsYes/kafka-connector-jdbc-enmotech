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
    private Payload payload;
    @JSONField(ordinal = 1)
    private Schema schema;

    public JsonDemo(Schema schema, Payload payload) {
        this.schema = schema;
        this.payload = payload;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
