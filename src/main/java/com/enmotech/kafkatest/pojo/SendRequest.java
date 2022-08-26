package com.enmotech.kafkatest.pojo;

/**
 * com.enmotech.kafkatest.pojo
 *
 * @author syf
 * @create 2022-08-25-14:24
 * @Description kafka-test
 */

public class SendRequest {
    private Integer time;
    private Integer frequency;
    private String topic_prefix;
    private Integer topic_quantity;
    private String[] topics;

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getTopic_prefix() {
        return topic_prefix;
    }

    public void setTopic_prefix(String topic_prefix) {
        this.topic_prefix = topic_prefix;
    }

    public Integer getTopic_quantity() {
        return topic_quantity;
    }

    public void setTopic_quantity(Integer topic_quantity) {
        this.topic_quantity = topic_quantity;
    }
}
