package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.loaihang;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class loaihangApiClient {
    @Getter
    private static final loaihangApiClient instance = new loaihangApiClient();

    private final String apiUrl = configLoader.getbaseapiurl() + "/loaihang";
    private final Gson gson = new Gson();
    public HttpClient httpClient = HttpClient.newBuilder().build();

    public List<loaihang> getAllLoaiHang() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<loaihang>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách loại hàng: " + response.statusCode());
    }

    public void themLoaiHang(loaihang lh) throws Exception {
        String json = gson.toJson(lh);
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

    public void suaLoaiHang(loaihang lh) throws Exception {
        String json = gson.toJson(lh);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + lh.getMaloai()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public void xoaLoaiHang(String maloai) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + maloai))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public List<loaihang> timKiemLoaiHang(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<loaihang>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tìm kiếm loại hàng: " + response.statusCode());
    }
}