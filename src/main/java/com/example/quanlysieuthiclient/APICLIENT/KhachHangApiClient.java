package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.KhachHang;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KhachHangApiClient {
    @Getter
    private static final KhachHangApiClient instance = new KhachHangApiClient();

    private final String apiUrl = configLoader.getbaseapiurl() + "/khachhang";

    // Cấu hình Gson đặc biệt để hỗ trợ kiểu LocalDate của Java 8
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) ->
                    new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .create();

    public HttpClient httpClient = HttpClient.newBuilder().build();

    // LẤY DANH SÁCH (GET)
    public List<KhachHang> getAllKhachHang() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<KhachHang>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tải danh sách KH: " + response.statusCode());
    }

    // THÊM (POST)
    public void themKhachHang(KhachHang kh) throws Exception {
        String json = gson.toJson(kh);
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

    // SỬA (PUT)
    public void suaKhachHang(KhachHang kh) throws Exception {
        String json = gson.toJson(kh);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + kh.getMaKH()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception(response.body());
        }
    }

    // XÓA (DELETE)
    public void xoaKhachHang(String maKH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + maKH))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200 ){
            throw new Exception(response.body());
        }
    }

    // TÌM KIẾM (GET có Parameter)
    public List<KhachHang> timKiemKhachHang(String keyword) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/search?keyword=" + keyword))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
            Type type = new TypeToken<List<KhachHang>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi tìm kiếm KH: " + response.statusCode());
    }
}
