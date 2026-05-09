package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.LichSuDoiQua;
import com.example.quanlysieuthiclient.DTO.QuaTang;
import com.example.quanlysieuthiclient.UTIL.configLoader;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoiQuaApiClient {
    @Getter
    private static final DoiQuaApiClient instance = new DoiQuaApiClient();
    private final String apiUrl = configLoader.getbaseapiurl() + "/doiqua";

    // Cấu hình Gson để xử lý thời gian chuẩn xác
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                    LocalDateTime.parse(json.getAsString()))
            .create();

    public HttpClient httpClient = HttpClient.newBuilder().build();

    // 1. Lấy kho quà tặng
    public List<QuaTang> getKhoQua() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/quatang")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<QuaTang>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách quà: " + response.statusCode());
    }

    // 2. Lấy lịch sử đổi quà của 1 khách hàng
    public List<LichSuDoiQua> getLichSu(String maKH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/lichsu/" + maKH)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<LichSuDoiQua>>() {}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy lịch sử: " + response.statusCode());
    }

    // 3. Thực hiện đổi quà (Sử dụng POST với tham số trên URL)
    public String doiQua(String maKH, String maQua) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/thuchien?maKH=" + maKH + "&maQua=" + maQua))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body(); // Trả về câu thông báo thành công
        } else {
            throw new Exception(response.body()); // Trả về thông báo lỗi (ví dụ: Thiếu điểm)
        }
    }
}