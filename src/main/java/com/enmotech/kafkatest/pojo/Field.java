package com.enmotech.kafkatest.pojo;

/**
 * com.enmotech.kafkatest.pojo
 *
 * @author syf
 * @create 2022-08-22-10:19
 * @Description kafka-test
 */

public class Field {
    private String type;
    private boolean optional;
    //字段名
    private String field;
    private String name;
    private int version;

    private Field(String type, boolean optional, String field) {
        this.type = type;
        this.optional = optional;
        this.field = field;
    }

    private Field(String type, boolean optional, String field, String name, int version) {
        this.type = type;
        this.optional = optional;
        this.field = field;
        this.name = name;
        this.version = version;
    }

    public static Field[] getFields(){
        Field[] fields = new Field[8];
        fields[0] = new Field("int32",false,"id");
        fields[1] = new Field("double",true,"temperature");
        fields[2] = new Field("double",true,"longitude");
        fields[3] = new Field("double",true,"latitude");
        fields[4] = new Field("double",true,"humidity");
        fields[5] = new Field("int64",true,"tabletime","org.apache.kafka.connect.data.Timestamp",1);
        fields[6] = new Field("string",true,"stringtime");
        fields[7] = new Field("string",true,"randomstring");
        return fields;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
