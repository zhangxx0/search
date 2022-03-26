package com.xinxin.search.spider;

import com.xinxin.search.dto.BossJobSearchDto;
import com.xinxin.search.esindex.BossJobIndex;

import java.io.IOException;
import java.util.List;

public interface BossJobService {
    String initdb();
    String initES();

    /**
     * 职位搜索
     * 使用RestHighLevelClient，7.15后弃用
     * @param bossJobSearchDto
     * @return
     */
    List<BossJobIndex> searchJobWithRestClient(BossJobSearchDto bossJobSearchDto) throws IOException;

    /**
     * 职位搜索2
     * 使用Elasticsearch Java API Client
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/introduction.html#introduction
     * Requirements:
     *  Java 8 or later.
     *  A JSON object mapping library to allow seamless integration of your application classes with the Elasticsearch API. The Java client has support for Jackson or a JSON-B library like Eclipse Yasson.
     * @param bossJobSearchDto
     * @return
     */
    List<BossJobIndex> searchJobWithESJavaAPIClient(BossJobSearchDto bossJobSearchDto) throws IOException;
}
