package com.tejchen.switchclient.helper;

import com.tejchen.switchcommon.helper.SerializeHelper;
import lombok.Cleanup;
import lombok.extern.java.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

@Log
public class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static String get(HttpClient client, String url, Map<String, String> param) {
        HttpGet get = new HttpGet(formatURL(url, param));
        String result = execute(client, get);
        return result;
    }

    public static String PostBody(HttpClient client, String url, Object form) {
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (form != null) {
            String body = SerializeHelper.serializeJson(form);
            post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        }
        String result = execute(client, post);
        return result;
    }

    private static String execute(HttpClient client, HttpUriRequest request){
        try {
            @Cleanup CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null && response.getEntity().getContent() != null){
                    String result = readerLines(response.getEntity().getContent()).reduce(String::concat).get();
                    return result;
                }
            }
        } catch (IOException e){
            logger.error("http connect err!", e);
        }
        return null;
    }

    private static String formatURL(String url, Map<String, String> param) {
        StringBuilder builder = new StringBuilder(url);
        if (param == null || param.isEmpty()) {
            return builder.toString();
        }
        if (builder.toString().endsWith("?")) {
            builder.append("?");
        }
        param.forEach((k, v) -> builder.append(String.format("%s=%s", k, v)));
        return builder.toString();
    }


    private static Stream<String> readerLines(InputStream inputStream) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(inputStream, "utf-8")).lines();
    }

}