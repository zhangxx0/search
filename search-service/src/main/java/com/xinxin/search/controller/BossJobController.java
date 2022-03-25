package com.xinxin.search.controller;

import com.xinxin.search.spider.BossJobService;
import com.xinxin.search.spider.BossJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class BossJobController {

    @Autowired
    BossJobService bossJobService;

    /**
     * http://localhost:9091/job/initdb
     * @return
     */
    @GetMapping("/initdb")
    public String initdb() {
        return bossJobService.initdb();
    }

    @GetMapping("/initES")
    public String initES() {
        return bossJobService.initES();
    }
}
