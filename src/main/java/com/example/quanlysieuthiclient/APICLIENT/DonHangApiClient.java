package com.example.quanlysieuthiclient.APICLIENT;

import com.example.quanlysieuthiclient.DTO.DonHang;
import com.example.quanlysieuthiclient.DTO.ChiTietDon;
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

public class DonHangApiClient {
    @Getter
    private static final DonHangApiClient instance = new DonHangApiClient();
    private final String apiUrl = configLoader.getbaseapiurl() + "/donhang";
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    // Lấy danh sách đơn hàng
    public List<DonHang> getAllDonHang() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<DonHang>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy danh sách đơn hàng");
    }

    // Lấy chi tiết của một đơn hàng
    public List<ChiTietDon> getChiTietByMa(String maDH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/" + maDH + "/chitiet")).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type type = new TypeToken<List<ChiTietDon>>(){}.getType();
            return gson.fromJson(response.body(), type);
        }
        throw new Exception("Lỗi lấy chi tiết đơn hàng");
    }

    // Thêm mới đơn hàng (Gửi kèm danh sách chi tiết)
    public void saveDonHang(DonHang dh, List<ChiTietDon> dsChiTiet) throws Exception {
        // Tạo một Wrapper object hoặc Map để gửi cả 2 thông tin cùng lúc
        var payload = java.util.Map.of("donHang", dh, "chitiet", dsChiTiet);
        String json = gson.toJson(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new Exception(response.body());
    }

    public void xoaDonHang(String maDH) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "/" + maDH)).DELETE().build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void updateDonHang(String maDH, DonHang dh, List<ChiTietDon> dsChiTiet) throws Exception {
        var payload = java.util.Map.of("donHang", dh, "chitiet", dsChiTiet);
        String json = gson.toJson(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + maDH))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new Exception(response.body());
    }
}