package org.budgiant.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    private static final String DATA_TYPE_JSON = "JSON";
    private static final String DATA_TYPE_FORM = "FORM";

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     */
    public static String sendGet(String url) {
        String result = "请求失败！";
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            if (response != null) {
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url 请求地址
     * @param map 请求参数(表单格式)
     * @return
     */
    public static String sendPostForm(String url, Map<String, String> map) {
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (map != null && map.entrySet().size() > 0) {
            List<NameValuePair> formParams = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httppost.setEntity(entity);
        }
        return sendPost(httppost);
    }


    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url 请求地址
     * @param map 请求参数(JSON格式)
     * @return
     */
    public static String sendPostJson(String url, Map<String, Object> map) {
        HttpPost httppost = new HttpPost(url);
//        httppost.setHeader("Content-Type","application/json");
        StringEntity entity = new StringEntity(JSON.toJSONString(map), Consts.UTF_8);
        httppost.setEntity(entity);
        entity.setContentType("application/json");
        return sendPost(httppost);
    }

    private static String sendPost(HttpPost httppost) {
        String result = "请求失败!";
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity1 = response.getEntity();
            result = EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return result;
    }

}
