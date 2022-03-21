package com.xinxin.search.spider;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BossTestService {

    static String url = String.format("https://www.zhipin.com/c101120200-p100101/?ka=search_100101");
//    https://www.zhipin.com/c101120200-p100101/?page=1&ka=page-1
//    https://www.zhipin.com/c101120200-p100101/?page=2&ka=page-2
//    https://www.zhipin.com/c101120200-p100101/?page=3&ka=page-next

    public static Request buildRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", "lastCity=101120200; wd_guid=a9fc39d3-5b7d-45b8-a63c-18197d7c78c8; historyState=state; _bl_uid=y4kqhz64xearedq5X7gm16O11531; __zp_seo_uuid__=529541bf-aa65-439d-a4ef-cfc0c162a818; __g=-; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1645512577,1645581492,1647823716; acw_tc=0bcb2f0116478756485243689e26084a69902e3bb7d89c866f5fa6494f7243; __l=r=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3D2lXYzOjEWhL4w6R5ch9IdACx0iM9cRMy5svXDNNQzZXcDENIINUdfQWBuwYvbjNd%26wd%3D%26eqid%3Dc40fd42600566711000000026237cb59&l=%2Fwww.zhipin.com%2Fqingdao%2F&s=3&g=&friend_source=0&s=3&friend_source=0; __zp_stoken__=d4eedYWo2TAsQUGRGIzp7dCkRYSZvNzoiQkpIYgYlZHYgd21zZwVEdlEcLGs9SAQhBSZ%2BcmFgL3AcQi8VUlkCSmZxPAVtCyBuXF0BWnA6VGA%2FQTpLaXECCTR%2BDSdOUSBqRx9MBnsbRQ1LBiE%3D; __c=1645512576; __a=32118873.1645512576..1645512576.35.1.35.35; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1647875757")
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

    public static void work() throws IOException {
//        Request request = buildRequest(url);
//        Response response = getResponse(request);
//
//        Map<String, String> map = new HashMap<>();
//        String html = response.body().string();
//        log.info(html);
//        Document document = Jsoup.parse(html);

        File input = new File("E:\\Code\\LearnCode\\search\\search-service\\src\\main\\resources\\html\\333.html");
        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");

        Elements elements = document.getElementsByClass("job-primary");
        for (Element element : elements) {
            log.info(element.toString());

            Elements jobTitle = element.getElementsByClass("job-title");
            log.info(jobTitle.get(0).attributes().get("title"));


            Attributes attributes = element.attributes();
            String key = attributes.get("title");
            String value = attributes.get("data-original");
//            map.put(key, value);
        }

//        <div class="info-primary">
//         <div class="primary-wrapper">
//          <div class="primary-box" href="/job_detail/5d9e11040ed1b7051nxy39q-EFZS.html" data-securityid="MjcRH8EwTBZpv-Q1sJUzJO5zulE_FfKVpXhA4XOheNA3A3kN9moMuzvjqp4WlGX_NfAxmEVI2eygUQvyxeG_nX_cpGp4Ksowrs7Fr7uYwnwfkB7n8w~~" data-jid="5d9e11040ed1b7051nxy39q-EFZS" data-itemid="1" data-lid="5sJJJMiq3Uz.search.1" data-jobid="188473042" data-index="0" ka="search_list_1" target="_blank">
//           <div class="job-title">
//            <span class="job-name"><a href="/job_detail/5d9e11040ed1b7051nxy39q-EFZS.html" title="开发工程师" target="_blank" ka="search_list_jname_1" data-securityid="MjcRH8EwTBZpv-Q1sJUzJO5zulE_FfKVpXhA4XOheNA3A3kN9moMuzvjqp4WlGX_NfAxmEVI2eygUQvyxeG_nX_cpGp4Ksowrs7Fr7uYwnwfkB7n8w~~" data-jid="5d9e11040ed1b7051nxy39q-EFZS" data-itemid="1" data-lid="5sJJJMiq3Uz.search.1" data-jobid="188473042" data-index="0">开发工程师</a></span>
//            <span class="job-area-wrapper"> <span class="job-area">青岛·市北区·凯德广场</span> </span>
//            <span class="job-pub-time"></span>
//           </div>
//           <div class="job-limit clearfix">
//            <span class="red">6-11K</span>
//            <p>1-3年<em class="vline"></em>大专</p>
//            <div class="info-publis">
//             <h3 class="name"><img class="icon-chat" src="https://z.zhipin.com/web/geek/resource/icon-chat-v2.png">赵女士<em class="vline"></em>赵经理</h3>
//            </div>
//            <button ka="cpc_job_list_chat_1" class="btn btn-startchat" href="javascript:;" data-url="/wapi/zpgeek/friend/add.json?securityId=MjcRH8EwTBZpv-Q1sJUzJO5zulE_FfKVpXhA4XOheNA3A3kN9moMuzvjqp4WlGX_NfAxmEVI2eygUQvyxeG_nX_cpGp4Ksowrs7Fr7uYwnwfkB7n8w~~&amp;jobId=5d9e11040ed1b7051nxy39q-EFZS&amp;lid=5sJJJMiq3Uz.search.1" redirect-url="/web/geek/chat?id=4aa9611c91a914a51XN-2NS7EVM~"> <img class="icon-chat icon-chat-hover" src="https://z.zhipin.com/web/geek/resource/icon-chat-hover-v2.png" alt=""> <span>立即沟通</span> </button>
//           </div>
//           <div class="info-detail"></div>
//          </div>
//         </div>
//         <div class="info-company">
//          <div class="company-text">
//           <h3 class="name"><a href="/gongsi/739fb851d07b307c1nxy39y0EA~~.html" title="前沿企业咨询招聘" ka="search_list_company_1_custompage" target="_blank">前沿企业咨询</a></h3>
//           <p><a href="/i100303/" class="false-link" target="_blank" ka="search_list_company_industry_1_custompage" title="培训机构行业招聘信息">培训机构</a><em class="vline"></em>不需要融资<em class="vline"></em>20-99人</p>
//          </div>
//          <a href="/gongsi/739fb851d07b307c1nxy39y0EA~~.html" ka="search_list_company_1_custompage_logo" target="_blank"><img class="company-logo" src="https://img.bosszhipin.com/beijin/icon/894ce6fa7e58d64d57e7f22d2f3a9d18afa7fcceaa24b8ea28f56f1bb14732c0.png?x-oss-process=image/resize,w_100,limit_0" alt=""></a>
//         </div>
//        </div>


        /*for (Map.Entry entry : map.entrySet()) {
            Request requestSub = buildRequest(entry.getValue().toString());
            Response responseSub = getResponse(requestSub);

            InputStream inputStream = responseSub.body().byteStream();

        }*/


    }

    public static void main(String[] args) {
        try {
            work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
