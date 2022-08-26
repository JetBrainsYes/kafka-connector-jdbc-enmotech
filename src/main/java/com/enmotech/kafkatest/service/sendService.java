package com.enmotech.kafkatest.service;

import com.enmotech.kafkatest.pojo.SendRequest;

/**
 * com.enmotech.kafkatest.service
 *
 * @author syf
 * @create 2022-08-23-17:32
 * @Description kafka-test
 */

public interface sendService {
    String send(int count,int target,String topic);
    void sendByTime(int time,int frequency,String[] topics);
    void sendByFrequency(int frequency,String[] topics);
}
