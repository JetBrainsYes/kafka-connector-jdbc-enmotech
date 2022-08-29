package com.enmotech.kafkatest.controller;
import com.enmotech.kafkatest.pojo.LogMessage;
import com.enmotech.kafkatest.pojo.SendRequest;
import com.enmotech.kafkatest.service.sendService;
import com.enmotech.kafkatest.util.SchemaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

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
public class Demo {
    @Autowired
    sendService sendServiceImpl;
    @Autowired
    SchemaUtil schemaUtil;

    //随机生成topic参数
    String[] topics;
    String prefix;

    @PostMapping("/send")
    public String send(@RequestBody SendRequest request){
        String message;
        long endTime;
        long beginTime = System.currentTimeMillis();
        //指定tpoic时
        if (request.getTopics()!=null){
            topics = request.getTopics();
        }
        //未指定topic，且未指定前缀或前缀未改变时
        else if (prefix == null ||!prefix.equals(request.getTopic_prefix())){
            prefix = request.getTopic_prefix();
            topics = schemaUtil.getRandomTopics(prefix,request.getTopic_quantity());
        }
        //未指定发送时间时
        if (request.getTime() ==null){
            if (request.getFrequency() == null){
                message = "未指定发送时间与数据数量，无法发送数据！";
                log.info("测试结果：{}",message);
                return "未指定发送时间与数据数量";
            }
            Map<String, Integer> map = sendServiceImpl.sendByFrequency(request.getFrequency(), topics);
            endTime = System.currentTimeMillis();
            message = new LogMessage(map,beginTime,endTime,"按数量发送").toString();
            log.info("测试结果：{}",message);
            return "本次生成的topic数量为："+topics.length+"\n"+"生成的topic名称为"+Arrays.toString(topics);
        }
        Map<String, Integer> map = sendServiceImpl.sendByTime(request.getTime(), request.getFrequency(), topics);
        endTime = System.currentTimeMillis();
        message = new LogMessage(map,beginTime,endTime,"按时间发送").toString();
        log.info("测试结果：{}",message);
        return "本次生成的topic数量为："+topics.length+"\n"+"生成的topic名称为"+ Arrays.toString(topics);
    }
}
