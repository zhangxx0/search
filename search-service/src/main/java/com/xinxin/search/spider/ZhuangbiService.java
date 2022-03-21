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
public class ZhuangbiService {

//    https://fabiaoqing.com/search/bqb/keyword/%E8%A3%85%E9%80%BC/type/bq/page/1.html
    static String url = String.format("https://fabiaoqing.com/search/bqb/keyword/%s/type/bq/page/%d.html", "装逼", 1);

    public static Request buildRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
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
        Request request = buildRequest(url);
        Response response = getResponse(request);

        Map<String, String> map = new HashMap<>();
        Document document = Jsoup.parse(response.body().string());
        Elements elements = document.getElementsByClass("ui image bqppsearch lazy");
        for (Element element : elements) {
            Attributes attributes = element.attributes();
//            <img class="ui image bqppsearch lazy" data-original="http://tva1.sinaimg.cn/bmiddle/415f82b9jw1fbjmu9d08gj20ku0jz794.jpg" src="/Public/lazyload/img/transparent.gif" title="别逗了我特么哪有书 - 考试挂科的原因好像找到了_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼表情" alt="别逗了我特么哪有书 - 考试挂科的原因好像找到了_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼_挂科_装逼表情" style="max-height:188;margin: 0 auto">
//            TODO 这个地方的第二个正则没看懂是什么东西，且它的代码本身报错，没看出什么玩意来
//                String key = attributes.get("title").replaceAll("\n", "").replaceAll("[/\\\\:*?|\\"<>]", "");
            String key = attributes.get("title").replaceAll("\n", "").replaceAll("[/\\\\:*?|\"<>]", "").substring(0, 10);
            String value = attributes.get("data-original");
            log.info(attributes.get("title").replaceAll("\n", "").replaceAll("[/\\\\:*?|\"<>]", ""));
//            log.info(value);
            map.put(key, value);
        }

        for (Map.Entry entry : map.entrySet()) {
            Request requestSub = buildRequest(entry.getValue().toString());
            Response responseSub = getResponse(requestSub);

            InputStream inputStream = responseSub.body().byteStream();

//            File file = new File("D:\image\" + key + ".png");
            File file = new File("D:\\image\\" + entry.getKey() + ".png");
            boolean bo = file.getParentFile().mkdirs();

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                int len;
                while ((len = inputStream.read()) != -1) {
                    outputStream.write(len);
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        try {
            work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
