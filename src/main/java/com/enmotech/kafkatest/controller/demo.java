package com.enmotech.kafkatest.controller;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.enmotech.kafkatest.pojo.JsonDemo;
import com.enmotech.kafkatest.pojo.SendRequest;
import com.enmotech.kafkatest.service.sendService;
import com.enmotech.kafkatest.util.SchemaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * com.enmotech.kafkatest.controller
 *
 * @author syf
 * @create 2022-08-18-15:04
 * @Description kafka-test-demo
 *  还可以添加和修改的功能：
 *      1.通过请求指定不同随机数据出现空值的概率
 *      2.schema和payload根据输入的参数修改格式
 */

@RestController
@Slf4j
public class demo {
    @Autowired
    sendService sendServiceImpl;
    @Autowired
    SchemaUtil schemaUtil;
    //计算数据数量
    int count = 0;
    String[] topics;
    String prefix;


    @PostMapping("/send")
    public String send(@RequestBody SendRequest request){
        if (request.getTopics()!=null){
            topics = request.getTopics();
        }else if (prefix == null ||!prefix.equals(request.getTopic_prefix())){
            prefix = request.getTopic_prefix();
            topics = schemaUtil.getRandomTopics(prefix,request.getTopic_quantity());
        }
        if (request.getTime() ==null){
            sendServiceImpl.sendByFrequency(request.getFrequency(),topics);
            return "本次生成的topic数量为："+topics.length+"\n"+"生成的topic名称为"+Arrays.toString(topics);
        }
        sendServiceImpl.sendByTime(request.getTime(), request.getFrequency(), topics);
        return "本次生成的topic数量为："+topics.length+"\n"+"生成的topic名称为"+ Arrays.toString(topics);
    }
}
