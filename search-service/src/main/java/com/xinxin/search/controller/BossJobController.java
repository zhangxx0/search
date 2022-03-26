package com.xinxin.search.controller;

import com.google.common.collect.Lists;
import com.xinxin.search.dto.BossJobSearchDto;
import com.xinxin.search.esindex.BossJobIndex;
import com.xinxin.search.spider.BossJobService;
import com.xinxin.search.spider.BossJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    /**
     * 职位搜索接口
     *
     * @return
     */
    @PostMapping("/searchJob")
    public List<BossJobIndex> searchJob(@RequestBody BossJobSearchDto bossJobSearchDto) {
        List<BossJobIndex> jobList = Lists.newArrayList();

        try {
            jobList = bossJobService.searchJobWithRestClient(bossJobSearchDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobList;
    }
}
