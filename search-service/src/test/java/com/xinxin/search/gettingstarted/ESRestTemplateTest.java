package com.xinxin.search.gettingstarted;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;

/**
 * @Author: hanko
 * @Date: 2020.12.7 10:43
 */
@Slf4j
@SpringBootTest
public class ESRestTemplateTest {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 通过ElasticsearchRestTemplate实现词频统计
     *
     */
    @Test
   public void aggregations_test(){

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("person-aggregate")
                .field("firstname");
        Query query = new NativeSearchQueryBuilder()
                .addAggregation(aggregation)
                .build();

        SearchHits<Person> searchHits = elasticsearchRestTemplate.search(query, Person.class);
        Aggregations aggregations = (Aggregations) searchHits.getAggregations().aggregations();
        Terms byCompanyAggregation = aggregations.get("person-aggregate");
        List<? extends Terms.Bucket> elasticBucket = byCompanyAggregation.getBuckets();
        elasticBucket.forEach(el -> {
            log.info("key:" + el.getKeyAsString());
            log.info("doc_count:" + el.getDocCount());
        });
    }
}
