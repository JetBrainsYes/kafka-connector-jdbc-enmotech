package com.enmotech.kafkatest.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * com.enmotech.kafkatest.config
 *
 * @author syf
 * @create 2022-08-30-15:08
 * @Description test
 */
@Component
public class KafkaConfig {
    //需要在主启动类上配置注解@EnableConfigurationProperties(KafkaProperties.class)
    @Autowired
    private KafkaProperties properties;

    @Bean
    public AdminClient getAdminClient(){
        AdminClient client = AdminClient.create(properties.buildAdminProperties());
        return client;
    }
}
