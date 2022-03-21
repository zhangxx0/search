package com.xinxin.search.spider;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ZhuangbiServiceTest {

    @Autowired
    ZhuangbiService zhuangbiService;

    @Test
    void work() {
        try {
            zhuangbiService.work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}