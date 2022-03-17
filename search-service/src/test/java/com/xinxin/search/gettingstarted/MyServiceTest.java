package com.xinxin.search.gettingstarted;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyServiceTest {

    @Autowired
    MyService myService;

    @Test
    void doWork() {
        myService.doWork();
    }
}