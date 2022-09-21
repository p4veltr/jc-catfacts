package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String url = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

        String jsonData = getFactsFromUrl(url);

        ObjectMapper mapper = new ObjectMapper();
        List<AnimalFacts> list;
        try {
            list = mapper.readValue(jsonData, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        list.stream().filter(fact -> fact.getUpvotes() > 0).forEach(System.out::println);
    }

    public static String getFactsFromUrl(String url) {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()) {
            CloseableHttpResponse response = httpClient.execute(request);
            return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}