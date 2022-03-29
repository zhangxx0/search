package com.xinxin.search.spider;

import com.xinxin.search.dto.BossJobSearchDto;
import com.xinxin.search.esindex.BossJobIndex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class BossJobServiceImplTest {

    @Autowired
    BossJobService bossJobService;

    @Test
    void searchJob() {
        BossJobSearchDto bossJobSearchDto = new BossJobSearchDto();
        bossJobSearchDto.setKeyword("java");
        bossJobSearchDto.setFinance("已上市");
        List<BossJobIndex> list = null;
        try {
//            list = bossJobService.searchJobWithRestClient(bossJobSearchDto);
            list = bossJobService.searchJobWithESJavaAPIClient(bossJobSearchDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(String.valueOf(list.size()));
    }
}