package com.xinxin.search.spider;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.common.collect.Lists;
import com.xinxin.search.dataobject.BossJob;
import com.xinxin.search.dto.BossJobSearchDto;
import com.xinxin.search.esindex.BossJobIndex;
import com.xinxin.search.repository.BossJobIndexRepository;
import com.xinxin.search.repository.BossJobRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class BossJobServiceImpl implements BossJobService {

    /**
     * 索引名称
     */
    static final String BOSS_JOB_INDEX = "boss_job_index";

    @Autowired
    BossJobRepository bossJobRepository;

    @Autowired
    BossJobIndexRepository bossJobIndexRepository;

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    // 获取页数
    static final Integer MAX = 1;
    // TODO 目前只能爬取前4页，后面的提示请稍后~~
    // 请求cookie
//    static final String cookie = "lastCity=101120200; wd_guid=a9fc39d3-5b7d-45b8-a63c-18197d7c78c8; historyState=state; _bl_uid=y4kqhz64xearedq5X7gm16O11531; __zp_seo_uuid__=529541bf-aa65-439d-a4ef-cfc0c162a818; __g=-; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1645512577,1645581492,1647823716; __l=r=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D2lXYzOjEWhL4w6R5ch9IdACx0iM9cRMy5svXDNNQzZXcDENIINUdfQWBuwYvbjNd%26wd%3D%26eqid%3Dc40fd42600566711000000026237cb59&l=%2Fwww.zhipin.com%2Fc101120200-p100101%2F%3Fka%3Dsearch_100101&s=3&g=&friend_source=0&s=3&friend_source=0; acw_tc=0a099d9a16480477812986406e019ac374c8e2129365537e59fdad48573bb7; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1648047788; __c=1645512576; __a=32118873.1645512576..1645512576.71.1.71.71; __zp_stoken__=c818dKQgpTFI7DVJtfApFaiN%2FQSMLe0Qwdw5pXGM3ZHsjDykgXVd3fWUDBmoIMGAhT11NOhxOLyV6e34NcWZuEGscfhx9TSsUd2YkQlQfJmxsOhJENj8ORXEQYBlnTANxDWR%2FThxEUEctOg0%3D";
    static final String cookie = "lastCity=101120200; wd_guid=a9fc39d3-5b7d-45b8-a63c-18197d7c78c8; historyState=state; _bl_uid=y4kqhz64xearedq5X7gm16O11531; __zp_seo_uuid__=529541bf-aa65-439d-a4ef-cfc0c162a818; __g=-; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1645512577,1645581492,1647823716; __l=r=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D2lXYzOjEWhL4w6R5ch9IdACx0iM9cRMy5svXDNNQzZXcDENIINUdfQWBuwYvbjNd%26wd%3D%26eqid%3Dc40fd42600566711000000026237cb59&l=%2Fwww.zhipin.com%2Fc101120200-p100101%2F%3Fka%3Dsearch_100101&s=3&g=&friend_source=0&s=3&friend_source=0; acw_tc=0a099d9a16480477812986406e019ac374c8e2129365537e59fdad48573bb7; __zp_stoken__=c818dKQgpTFI7b05cCndSaiN%2FQSMLYTRoE2JpXGM3ZG4MYjZdXVd3fWUDHGcqHld1T11NOhxOLzsRChYNYDRZNHw%2BdWc3Hy4MWWkcQlQfJmxsOkZzGB0DX3EQYBlnTANxDWR%2FThxEUEctOg0%3D; __c=1645512576; __a=32118873.1645512576..1645512576.72.1.72.72; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1648049010";


    static String url = String.format("https://www.zhipin.com/c101120200-p100101/?ka=search_100101");
//    https://www.zhipin.com/c101120200-p100101/?page=1&ka=page-1
//    https://www.zhipin.com/c101120200-p100101/?page=2&ka=page-2
//    https://www.zhipin.com/c101120200-p100101/?page=3&ka=page-next

    public static Request buildRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .addHeader(":authority", "www.zhipin.com")
                .addHeader(":method", "GET")
                .addHeader(":path", url.substring(url.indexOf("com") + 3))
                .addHeader(":scheme", "https")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36")
                .build();
        return request;
    }

    public static Response getResponse(Request request) {
        Response response = null;
        try {
            OkHttpClient client = new OkHttpClient();

            response = client.newCall(request).execute();
//            System.out.println(response.body().string());

        } catch (Exception e) {
            e.printStackTrace();

        }
        return response;
    }

    public void work(String url) throws IOException {
        Request request = buildRequest(url);
        Response response = getResponse(request);

        Map<String, String> map = new HashMap<>();
        String html = response.body().string();
        log.info(html);
        Document document = Jsoup.parse(html);

        // 本地存储文件
//        File input = new File("E:\\Code\\LearnCode\\search\\search-service\\src\\main\\resources\\html\\333.html");
//        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");

        Elements elements = document.getElementsByClass("job-primary");
        int i = 1;
        for (Element element : elements) {
//            log.info("*******************************************************");
//            log.info("第" + i + "个职位");
//            log.info(element.toString());
            BossJob bossJob = new BossJob();

            Elements jobTitle = element.getElementsByClass("job-name");
            // 职位名称
//            log.info(jobTitle.get(0).getElementsByTag("a").get(0).html());
            // 工作地域
            Elements jobArea = element.getElementsByClass("job-area");
//            log.info(jobArea.html());

            Elements jobLimit = element.getElementsByClass("job-limit");
            // 薪资
            Element salary = jobLimit.get(0).getElementsByClass("red").get(0);
//            log.info(salary.html());
            // 年限
            Element ageLimit = jobLimit.get(0).getElementsByTag("p").get(0);
//            log.info(ageLimit.html());
//            log.info(ageLimit.html().substring(0, ageLimit.html().indexOf("<em")));
            // 学历
//            log.info(ageLimit.html().substring(ageLimit.html().indexOf("</em>") + 5));
            // 招聘人及职位
            Element recruiter = jobLimit.get(0).getElementsByTag("h3").get(0);
//            log.info(recruiter.html());
//            log.info(recruiter.html().substring(recruiter.html().indexOf("\">") + 2, recruiter.html().indexOf("<em")));
//            log.info(recruiter.html().substring(recruiter.html().indexOf("</em>") + 5));

            Elements companyInfo = element.getElementsByClass("info-company");

            // 公司名称
//            log.info(companyInfo.get(0).getElementsByTag("a").get(0).html());
            // 公司行业
//            log.info(companyInfo.get(0).getElementsByTag("a").get(1).html());
            // 公司logo
//            log.info(companyInfo.get(0).getElementsByClass("company-logo").attr("src"));
            // 融资情况 非必填项
            // 规模
//            <p><a href="/i100303/" class="false-link" target="_blank" ka="search_list_company_industry_1_custompage" title="培训机构行业招聘信息">培训机构</a><em class="vline"></em>不需要融资<em class="vline"></em>20-99人</p>
            String companyText = companyInfo.get(0).getElementsByTag("p").html().replaceAll("<em class=\"vline\"></em>", "|");
//            log.info(companyText);
            String[] companyTextArr = companyText.split("\\|");

            Elements tags = element.getElementsByClass("tag-item");
            Elements welfare = element.getElementsByClass("info-desc");
            // 技术标签
            StringBuilder sb = new StringBuilder();
            for (Element tag : tags) {
                sb.append(tag.html());
                sb.append(",");
            }
//            log.info(sb.toString());
            // 福利
//            log.info(welfare.html());

            bossJob = BossJob.builder()
                    .name(jobTitle.get(0).getElementsByTag("a").get(0).html())
                    .area(jobArea.html())
                    .salary(salary.html())
                    .agelimit(ageLimit.html().substring(0, ageLimit.html().indexOf("<em")))
                    .education(ageLimit.html().substring(ageLimit.html().indexOf("</em>") + 5))
                    .recruiter(recruiter.html().substring(recruiter.html().indexOf("\">") + 2, recruiter.html().indexOf("<em")))
                    .recruiterPosition(recruiter.html().substring(recruiter.html().indexOf("</em>") + 5))
                    .companyName(companyInfo.get(0).getElementsByTag("a").get(0).html())
                    .companyType(companyInfo.get(0).getElementsByTag("a").get(1).html())
                    .companyLogo(companyInfo.get(0).getElementsByClass("company-logo").attr("src"))
                    .tags(sb.toString())
                    .welfare(welfare.html())
                    .build();
            if (companyTextArr.length > 2) {
                bossJob.setFinance(companyTextArr[1]);
                bossJob.setCompanySize(companyTextArr[2]);
//                log.info(companyTextArr[1]);
//                log.info(companyTextArr[2]);
            } else {
                bossJob.setCompanySize(companyTextArr[1]);
//                log.info(companyTextArr[1]);
            }

            bossJob.setCreateDate(new Date());
//            log.info(bossJob.toString());

            bossJobRepository.save(bossJob);

            i++;
        }


        /*for (Map.Entry entry : map.entrySet()) {
            Request requestSub = buildRequest(entry.getValue().toString());
            Response responseSub = getResponse(requestSub);

            InputStream inputStream = responseSub.body().byteStream();

        }*/


    }

    public static void main(String[] args) {
//        try {
        // TODO 构建多页面循环，可设置爬取页面数目
        System.out.println(url.substring(url.indexOf("com") + 3));

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    @Transactional
    public String initdb() {
        String result = "成功";
        try {
            // 构建多页面循环，可设置爬取页面数目
            for (int i = 1; i <= MAX; i++) {
                /**
                 * 构造页面的URL
                 * 其中cookie可能是临时的，随时可能失效
                 */
                String url = String.format("https://www.zhipin.com/c101120200-p100101/?page=%d&ka=page-%d", i, i);
                log.info("请求url：" + url);

                Random r = new Random();

                try {
                    Long sleepSecond = (r.nextInt(5) + 1) * 1000L;
                    log.info("睡眠秒数：" + sleepSecond);
                    Thread.sleep(sleepSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                work(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public String initES() {

        try {
            // TODO 修改为分页获取与批量插入ES
            List<BossJob> bossJobs = bossJobRepository.findAll();
            for (BossJob bossJob : bossJobs) {
                BossJobIndex bossJobIndex = new BossJobIndex();
                BeanUtils.copyProperties(bossJob, bossJobIndex);

                bossJobIndexRepository.save(bossJobIndex);
            }
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    /**
     * 职位搜索
     *
     * @param bossJobSearchDto
     * @return
     */
    @Override
    public List<BossJobIndex> searchJobWithRestClient(BossJobSearchDto bossJobSearchDto) throws IOException {
        List<BossJobIndex> jobList = Lists.newArrayList();

        SearchRequest searchRequest = new SearchRequest(BOSS_JOB_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // and or 整合查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 全量 QueryBuilders.matchAllQuery()
        // 单字段模糊 两种创建方式
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", bossJobSearchDto.getKeyword());
        MatchQueryBuilder nameMatchQB = QueryBuilders.matchQuery("name", bossJobSearchDto.getKeyword())
                .fuzziness(Fuzziness.AUTO);

        // 多字段模糊查询
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery(bossJobSearchDto.getKeyword(), "name", "companyName");

        // 精确查询
        TermQueryBuilder financeTermQB = QueryBuilders
                .termQuery("finance.keyword", bossJobSearchDto.getFinance());

        boolQueryBuilder.must(multiMatchQueryBuilder)
                .must(financeTermQB);

        searchSourceBuilder.query(boolQueryBuilder);
//        searchSourceBuilder.trackTotalHits(true);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        // 排序
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.DESC));

        searchRequest.source(searchSourceBuilder);

        log.info("+++++++++++++++++++++++++++++");
        log.info(searchSourceBuilder.toString());
        log.info("+++++++++++++++++++++++++++++");

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info(String.valueOf(searchResponse.getHits().getTotalHits().value));
        searchResponse.getHits().forEach(res -> {
//            log.info(res.toString());
        });

        log.info("client:" + restHighLevelClient);
//        restHighLevelClient
        return jobList;
    }

    /**
     * Elasticsearch Java API Client的基本使用方法参见：
     * https://blog.csdn.net/anjiongyi/article/details/123391835
     *
     * @param bossJobSearchDto
     * @return
     * @throws IOException
     */
    @Override
    public List<BossJobIndex> searchJobWithESJavaAPIClient(BossJobSearchDto bossJobSearchDto) throws IOException {
        List<BossJobIndex> jobList = Lists.newArrayList();


        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        co.elastic.clients.elasticsearch.core.SearchResponse<BossJobIndex> search = client.search(s -> s
                        .index(BOSS_JOB_INDEX)
                        .query(q -> q
                                .bool(b -> b
                                        .must(must -> must
                                                .multiMatch(m -> m
                                                        .fields("name", "companyName")
                                                        .query(bossJobSearchDto.getKeyword())
                                                )

                                        )
                                        .must(must -> must
                                                .term(t -> t
                                                        .field("finance.keyword")
                                                        .value(v -> v.stringValue(bossJobSearchDto.getFinance()))
                                                )
                                        )

                                )
                        ),
                BossJobIndex.class);

        /**
         * com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "_class" (class com.xinxin.search.esindex.BossJobIndex), not marked as ignorable (20 known properties: "remark", "updateId", "education", "salary", "name", "welfare", "area", "recruiterPosition", "companyType", "tags", "finance", "id", "agelimit", "companySize", "companyName", "recruiter", "createId", "companyLogo", "createDate", "updateDate"])
         * 在index document上添加：
         * @JsonIgnoreProperties(ignoreUnknown = true)
         */

        log.info(String.valueOf(search.hits().total().value()));
        log.info(String.valueOf(search.hits().hits().size()));
        for (Hit<BossJobIndex> hit : search.hits().hits()) {
            log.info(String.valueOf(hit.source()));
        }

        return jobList;
    }
}
