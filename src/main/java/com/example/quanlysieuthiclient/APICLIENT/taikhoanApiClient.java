package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.taikhoan;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import lombok.Getter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class taikhoanApiClient {
    @Getter
    private static final taikhoanApiClient instance = new taikhoanApiClient();
    private final String apiUrl = configLoader.getbaseapiurl() + "/taikhoan/login";
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public boolean kiemTraDangNhap(String taikhoan, String matkhau) throws Exception {
        taikhoan tk = new taikhoan(taikhoan, matkhau);
        String json = gson.toJson(tk);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}