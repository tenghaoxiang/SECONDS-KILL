package top.haibaraai.secondsKill.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装http get post
 */
public class HttpUtils {

    private static final Gson gson = new Gson();

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 封装get方法
     * @param url
     * @return
     */
    public static Map<String, Object> doGet(String url) {
        Map<String, Object> map = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) //连接超时时间
                .setConnectionRequestTimeout(5000) //请求获取数据的超时时间
                .setRedirectsEnabled(true) //允许重定向
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String jsonResult = EntityUtils.toString(entity);
                map = gson.fromJson(jsonResult, map.getClass());
            }
        } catch (Exception e) {
            logger.error("While do http get occur: " + e);
        }finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("While close http client occur: " + e);
            }
        }
        return map;
    }

    /**
     * 封装post方法，失败时返回null
     * @param url
     * @param data
     * @return
     */
    public static String doPost(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) //连接超时时间
                .setConnectionRequestTimeout(5000) //请求获取数据的超时时间
                .setRedirectsEnabled(true) //允许重定向
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                return result;
            }
        } catch (Exception e) {
            logger.error("While do http post occur: " + e);
        }finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                logger.error("While close http client occur: " + e);
            }
        }
        return null;
    }

}
