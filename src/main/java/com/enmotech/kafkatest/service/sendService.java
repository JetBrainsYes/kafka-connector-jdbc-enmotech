package com.enmotech.kafkatest.service;

import com.enmotech.kafkatest.pojo.SendRequest;

import java.util.Map;

/**
 * com.enmotech.kafkatest.service
 *
 * @author syf
 * @create 2022-08-23-17:32
 * @Description kafka-test
 */

public interface sendService {
    String send(int count,int target,String topic);
    Map<String,Integer> sendByTime(Integer time,Integer frequency,String[] topics);
    Map<String, Integer> sendByFrequency(int frequency, String[] topics);
}
