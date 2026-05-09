package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.chucvu;
import com.example.quanlysieuthiclient.DTO.khuyenmai;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class khuyenmaiApiClient {
        @Getter
        private static final khuyenmaiApiClient instance=new khuyenmaiApiClient();
        private final String apiUrl= configLoader.getbaseapiurl()+ "/khuyenmai";
        private final Gson gson=new Gson();
        public HttpClient httpClient=HttpClient.newBuilder().build();

        public List<khuyenmai> getAllKhuyenMai() throws Exception {
            HttpRequest request= HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response=httpClient.send(request,HttpResponse.BodyHandlers.ofString());

            if(response.statusCode()==200){
                Type type=new TypeToken<List<khuyenmai>>(){}.getType();
                return  gson.fromJson(response.body(),type);
            }
            throw new Exception("lỗi lấy danh sách khuyến mại , mã lỗi:"+response.statusCode());
        }
    // Thêm
    public void themKhuyenMai(khuyenmai km) throws Exception {
        String json = gson.toJson(km);
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

    // Sửa
    public void suaKhuyenMai(khuyenmai km) throws Exception {
        String json = gson.toJson(km);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + km.getMakhuyenmai()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    // Xóa
    public void xoaKhuyenMai(String makhuyenmai) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + makhuyenmai))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    // Tìm kiếm
    public List<khuyenmai> timKiemKhuyenMai(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<khuyenmai>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tìm kiếm: " + response.statusCode());
    }
}
