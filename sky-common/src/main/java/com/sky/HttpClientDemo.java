package com.sky;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.MemoryType;
import java.net.URISyntaxException;

public class HttpClientDemo {

    public static void main(String[] arg) throws Exception{
//        httpGet();
        //System.out.println(MediaType.APPLICATION_JSON.toString());
//        httpPost();
        mpLogin();
    }

    private static void mpLogin() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String code = "0a32vyll2qIvLd4ZzUll2mlhSl32vylI";
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=wx1c759351c7cfba06&serect=7fbaf60f8020696ace7a765bc16dd846&" +
                "js_code=%s" +
                "&grant_type=authorization_code",code);

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        HttpEntity entity = response.getEntity();
        String responseBodyJson = EntityUtils.toString(entity);
        JSONObject jsonObject = JSON.parseObject(responseBodyJson);
        String openid = jsonObject.getString("openid");
        System.out.println("openid = " + openid);
        response.close();
        httpClient.close();
    }

    private static void httpGet() throws IOException {
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = "+statusCode);
        HttpEntity entity = response.getEntity();
        System.out.println("EntityUtils.toString(entity)="+ EntityUtils.toString(entity));
        response.close();
        httpClient.close();
    }

    public static void httpPost() throws IOException {
        //1.创建client对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        StringEntity stringEntity=buildData();
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = "+statusCode);
        HttpEntity entity = response.getEntity();
        System.out.println("EntityUtils.toString(entity)="+ EntityUtils.toString(entity));
        response.close();
        httpClient.close();
    }

    private static StringEntity buildData() throws UnsupportedEncodingException {
        JSONObject jsonData = new JSONObject();
        jsonData.put("password","123456");
        jsonData.put("username","admin");
        String jsonDataString = jsonData.toJSONString();
        StringEntity stringEntity = new StringEntity(jsonDataString);
        //请求体的编码为utf-8
        stringEntity.setContentEncoding("utf-8");
        //请求体的格式
        stringEntity.setContentType(MediaType.APPLICATION_JSON.toString());
        return stringEntity;
    }
}