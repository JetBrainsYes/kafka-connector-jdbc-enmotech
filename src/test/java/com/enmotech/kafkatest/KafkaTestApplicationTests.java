package com.enmotech.kafkatest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class KafkaTestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test(){
        Random random = new Random();
        for (int i = 0; i <100 ; i++) {
            System.out.println(random.nextDouble()*100);
        }
    }
}
