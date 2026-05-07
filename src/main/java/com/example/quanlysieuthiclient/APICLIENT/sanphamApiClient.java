package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.sanpham;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class sanphamApiClient {
    @Getter
    private static final sanphamApiClient instance = new sanphamApiClient();

    private final String apiUrl = configLoader.getbaseapiurl() + "/sanpham";

    // Đã nâng cấp Gson: Ép kiểu format ngày tháng yyyy-MM-dd để tránh lỗi do Java Date
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public HttpClient httpClient = HttpClient.newBuilder().build();

    public List<sanpham> getAllSanPham() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<sanpham>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách sản phẩm: " + response.statusCode());
    }

    public void themSanPham(sanpham sp) throws Exception {
        String json = gson.toJson(sp);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public void suaSanPham(sanpham sp) throws Exception {
        String json = gson.toJson(sp);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + sp.getMasanpham()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public void xoaSanPham(String masanpham) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + masanpham))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public List<sanpham> timKiemSanPham(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<sanpham>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tìm kiếm sản phẩm: " + response.statusCode());
    }
}