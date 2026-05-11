package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.nhacungcap;
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

public class nhacungcapApiClient {
    @Getter
    private static final nhacungcapApiClient instance = new nhacungcapApiClient();

    private final String apiUrl = configLoader.getbaseapiurl() + "/nhacungcap";
    private final Gson gson = new Gson();
    public HttpClient httpClient = HttpClient.newBuilder().build();

    public List<nhacungcap> getAllNhaCungCap() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<nhacungcap>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách nhà cung cấp: " + response.statusCode());
    }

    public void themNhaCungCap(nhacungcap ncc) throws Exception {
        String json = gson.toJson(ncc);
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

    public void suaNhaCungCap(nhacungcap ncc) throws Exception {
        String json = gson.toJson(ncc);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + ncc.getManhacungcap()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public void xoaNhaCungCap(String manhacungcap) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + manhacungcap))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    public List<nhacungcap> timKiemNhaCungCap(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<nhacungcap>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tìm kiếm nhà cung cấp: " + response.statusCode());
    }
}