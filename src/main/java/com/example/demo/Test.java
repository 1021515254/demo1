package com.example.demo;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * TODO
 *
 * @author <a href="mailto:lucky_zhang_yu@163.com">lucky</a>
 * @since 1.0.0
 */
public class Test {
    // https://m.1234567.com.cn/data/FundSuggestList.js

    public static void main(String[] args) {
        String url = "https://m.1234567.com.cn/data/FundSuggestList.js";
        String str = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        CloseableHttpResponse crossResponse;
        try {
            crossResponse = httpClient.execute(httpGet);
            HttpEntity crossEntity = crossResponse.getEntity();
            str = EntityUtils.toString(crossEntity, "utf-8");
            str = str.replace("\n", "");
            str = str.replace("\t", "");
            str = str.replace("\r", "");
            str = str.replace(" ", "");
            str = str.replace("FundSuggestList(", "");
            str = str.replace(")", "");
            JSONObject jsonObject = (JSONObject) JSONArray.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
