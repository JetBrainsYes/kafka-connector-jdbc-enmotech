package com.enmotech.kafkatest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.enmotech.kafkatest.pojo.JsonDemo;
import com.enmotech.kafkatest.pojo.LogMessage;
import com.enmotech.kafkatest.pojo.SendRequest;
import com.enmotech.kafkatest.service.sendService;
import com.enmotech.kafkatest.util.SchemaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * com.enmotech.kafkatest.service.impl
 *
 * @author syf
 * @create 2022-08-23-17:33
 * @Description kafka-test
 */
@Component
@Slf4j
public class sendServiceImpl implements sendService {
    @Autowired
    SchemaUtil schemaUtil;
    //数据库数据对象
    @Autowired
    JsonDemo jsonDemo;
    String message;
    //记录数据ID
    int count;

    Map<String,Integer> topicMap;
    @Autowired
    private KafkaTemplate<String, String> template;

    @Override
    public String send(int count,int target,String topic) {
        //给jsonDemo中的属性赋值，生成数据
        schemaUtil.setPayload(count,jsonDemo.getPayload(),target);
        //配置序列化时不忽略null
        message = JSONObject.toJSONString(jsonDemo, SerializerFeature.WriteMapNullValue);
        //将消息发送到Kafka服务器的名称为“test”的Topic中
        this.template.send(topic, message);
        log.info("message: {}", message);
        return message;
    }

    @Override
    public Map<String,Integer> sendByTime(Integer time,Integer frequency,String[] topics) {
        topicMap = new HashMap<String, Integer>();
        for (int i = 0; i < topics.length; i++) {
            topicMap.put(topics[i],0);
        }
        long l = System.currentTimeMillis();
        long target = l+(time-1)*1000;
        //当未指定每秒数据量时
        if (frequency == null){
            while (l<target){
                int temp = (int) (Math.random()*(topics.length));
                send(count,2,topics[temp]);
                topicMap.put(topics[temp],(topicMap.get(topics[temp])+1));
                count++;
                l = System.currentTimeMillis();
            }
        }else {
            while (l < target) {
                sendDataCircularly(frequency,topics,topicMap);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                l = System.currentTimeMillis();
            }
        }
        return topicMap;
    }

    @Override
    public Map<String, Integer> sendByFrequency(int frequency, String[] topics) {
        topicMap = new HashMap<String, Integer>();
        for (int i = 0; i < topics.length; i++) {
            topicMap.put(topics[i],0);
        }
        /*for (int i = 0; i < frequency; i++) {
            int temp = (int) (Math.random()*(topics.length));
            send(count,2,topics[temp]);
            topicMap.put(topics[temp],(topicMap.get(topics[temp])+1));
            count++;
        }*/
        sendDataCircularly(frequency,topics,topicMap);
        return topicMap;
    }

    public void sendDataCircularly(int frequency,String[] topics,Map<String,Integer> map){
        for (int i = 0; i < frequency; i++) {
            int temp = (int) (Math.random()*(topics.length));
            send(count,2,topics[temp]);
            topicMap.put(topics[temp],(topicMap.get(topics[temp])+1));
            count++;
        }
    }
}
