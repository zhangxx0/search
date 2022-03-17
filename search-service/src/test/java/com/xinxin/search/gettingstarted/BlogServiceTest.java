package com.xinxin.search.gettingstarted;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 基本是一些单条件的查询，意义不大
 *
 */
@Slf4j
@SpringBootTest
class BlogServiceTest {

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 创建或更新index
     *
     * @throws IOException
     */
    @Test
    void create_index() throws IOException {
        IndexRequest indexRequest = new IndexRequest("high-index01");
        Blog blog = Blog.builder().id("03").title("你世界好").build();
        indexRequest.id("3");
        indexRequest.source(JSON.toJSONString(blog), XContentType.JSON);
        indexRequest.timeout("1s");
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info(indexResponse.getResult().toString());

    }

    /**
     * 查询所有matchAllQuery
     *
     * @throws IOException
     */
    @Test
    void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("high-index01");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits().forEach(res -> {
            log.info(res.toString());
        });

    }

    /**
     * 精确查询termQuery
     *
     * @throws IOException
     */
    @Test
    void termQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("high-index01");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("id", "1"));
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits().forEach(res -> {
            log.info(res.toString());
        });

    }

    /**
     * 模糊查询matchQuery
     *
     * @throws IOException
     */

    @Test
    void matchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("high-index01");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", "你好");
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits().forEach(res -> {
            log.info(res.toString());
        });
    }


    /**
     * 高亮查询
     *
     * @throws IOException
     */
    @Test
    void highlight() throws IOException {

        SearchRequest searchRequest = new SearchRequest("high-index01");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.highlighter(highlightBuilder());
        searchSourceBuilder.query(new MatchQueryBuilder("title", "你好"));

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        printHits(searchResponse);
    }


    /**
     * 词频统计aggregate
     */
    @Test
    public void aggregate() throws IOException {
        SearchRequest searchRequest = new SearchRequest("person-index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("person-aggregate")
                .field("firstname");
        searchSourceBuilder.aggregation(aggregation);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms byCompanyAggregation = aggregations.get("person-aggregate");
        List<? extends Terms.Bucket> elasticBucket = byCompanyAggregation.getBuckets();
        elasticBucket.forEach(el -> {
            log.info("key:" + el.getKeyAsString());
            log.info("doc_count:" + el.getDocCount());

        });

    }


    /**
     * 词频统计term vectors
     */
    @Test
    public void term_vectors() throws IOException {
        TermVectorsRequest request = new TermVectorsRequest("my-index-000001", "1");
        request.setFieldStatistics(true);
        request.setTermStatistics(true);
        request.setPositions(true);
        request.setOffsets(true);
        request.setPayloads(true);

        TermVectorsResponse response =
                restHighLevelClient.termvectors(request, RequestOptions.DEFAULT);

        for (TermVectorsResponse.TermVector tv : response.getTermVectorsList()) {
            String fieldname = tv.getFieldName();
            int docCount = tv.getFieldStatistics().getDocCount();
            long sumTotalTermFreq =
                    tv.getFieldStatistics().getSumTotalTermFreq();
            long sumDocFreq = tv.getFieldStatistics().getSumDocFreq();
            if (tv.getTerms() != null) {
                List<TermVectorsResponse.TermVector.Term> terms =
                        tv.getTerms();
                for (TermVectorsResponse.TermVector.Term term : terms) {
                    String termStr = term.getTerm();
                    System.out.println("termStr = " + termStr);
                    int termFreq = term.getTermFreq();
                    int docFreq = term.getDocFreq();
                    System.out.println("docFreq = " + docFreq);
                    long totalTermFreq = term.getTotalTermFreq();
//                    float score = term.getScore();
                    if (term.getTokens() != null) {
                        List<TermVectorsResponse.TermVector.Token> tokens =
                                term.getTokens();
                        for (TermVectorsResponse.TermVector.Token token : tokens) {
                            int position = token.getPosition();
                            int startOffset = token.getStartOffset();
                            int endOffset = token.getEndOffset();
                            String payload = token.getPayload();
                        }
                    }
                }
            }
        }

    }





    /* 私有方法*/
    private HighlightBuilder highlightBuilder() {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title");
        highlightBuilder.field(highlightTitle);
        highlightBuilder.preTags("<font size=\"3\" color=\"red\">");
        highlightBuilder.postTags("</font>");
        return highlightBuilder;
    }

    private void printHits(SearchResponse searchResponse) {
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits.getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight = highlightFields.get("title");
            Text[] fragments = highlight.fragments();
            String fragmentString = fragments[0].string();
            log.info(fragmentString);
        }
    }


}