package com.enmotech.kafkatest.pojo;

import org.springframework.stereotype.Component;

/**
 * com.enmotech.kafkatest.pojo
 *
 * @author syf
 * @create 2022-08-23-14:07
 * @Description kafka-test
 */

public class payload {
    private int id;
    private Double longitude;
    private Double latitude;
    private Double temperature;
    private Double humidity;
    private long tabletime;
    private String stringtime;
    private String randomstring;

    public long getTabletime() {
        return tabletime;
    }

    public void setTabletime(long tabletime) {
        this.tabletime = tabletime;
    }

    public String getRandomstring() {
        return randomstring;
    }

    public void setRandomstring(String randomstring) {
        this.randomstring = randomstring;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }


    public String getStringtime() {
        return stringtime;
    }

    public void setStringtime(String stringtime) {
        this.stringtime = stringtime;
    }


}
