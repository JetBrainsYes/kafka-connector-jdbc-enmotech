package com.enmotech.kafkatest.service;

import java.util.Map;

/**
 * com.enmotech.kafkatest.service
 *
 * @author syf
 * @create 2022-08-23-17:32
 * @Description kafka-test
 */

public interface SendService {
    String send(int count,int target,String topic);
    Map<String,Integer> sendByTime(Integer time,Integer frequency,String[] topics);
    Map<String, Integer> sendByFrequency(int frequency, String[] topics);
    void sendDataCircularly(int frequency,String[] topics,Map<String,Integer> map);
}
