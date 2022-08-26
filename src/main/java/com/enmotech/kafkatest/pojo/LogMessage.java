package com.enmotech.kafkatest.pojo;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * com.enmotech.kafkatest.pojo
 *
 * @author syf
 * @create 2022-08-26-16:58
 * @Description test
 */

public class LogMessage {
    private Map<String,Integer> map;
    private long beginTime;
    private long endTime;
    private String testMode;

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String bt = sdf.format(beginTime);
        String et = sdf.format(endTime);
        int count = 0;
        for (int value : map.values()) {
            count+=value;
        }
        return "测试开始时间为" +bt+"\n"+
                "测试结束时间为" + et +"\n"+
                "测试用时为" + (endTime-beginTime) +"\n"+
                "测试模式为" + testMode +"\n"+
                "发送数据总数："+ count+"\n"+
                "测试详情：" + map;
    }

    public LogMessage(Map<String, Integer> map, long beginTime, long endTime, String testMode) {
        this.map = map;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.testMode = testMode;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }
}
